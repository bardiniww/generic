package com.bardiniww.customer;

import com.bardiniww.exception.DuplicateResourceException;
import com.bardiniww.exception.RequestValidationException;
import com.bardiniww.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;


    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDAO);
    }

    @Test
    void canFindById() {
        // Given
        long id = 1L;
        Customer customer = new Customer(
                id,
                "Nick Tschernikow",
                20,
                "foo@email.com"
        );
        when(customerDAO.findById(id)).thenReturn(Optional.of(customer));

        // When
        final Customer actual = underTest.findById(id);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void throwsExceptionWhenFindByIdReturnsEmpty() {
        // Given
        long id = 1L;
        when(customerDAO.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> underTest.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with passed id [%s] not found".formatted(id));
    }

    @Test
    void findAll() {
        // When
        underTest.findAll();

        // Then
        verify(customerDAO).findAll();
    }

    @Test
    void register() {
        // Given
        final String email = "foo@email.com";
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "Nick Tschernikow",
                20,
                email
        );
        when(customerDAO.existsByEmail(email)).thenReturn(Boolean.FALSE);

        // When
        underTest.register(customerRegistrationRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).save(customerArgumentCaptor.capture());

        final Customer actual = customerArgumentCaptor.getValue();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(customerRegistrationRequest.name());
        assertThat(actual.getEmail()).isEqualTo(customerRegistrationRequest.email());
        assertThat(actual.getAge()).isEqualTo(customerRegistrationRequest.age());
    }

    @Test
    void throwsExceptionWhenRegisterWithBusyEmail() {
        // Given
        final String email = "foo@email.com";
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                "Nick Tschernikow",
                20,
                email
        );
        when(customerDAO.existsByEmail(email)).thenReturn(Boolean.TRUE);

        // When
        assertThatThrownBy(() -> underTest.register(customerRegistrationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Passed email already taken");

        // Then
        verify(customerDAO, never()).save(any());
    }

    @Test
    void deleteById() {
        // Given
        long id = 1L;
        when(customerDAO.existsById(id)).thenReturn(Boolean.TRUE);

        // When
        underTest.deleteById(id);

        // Then
        verify(customerDAO).deleteById(id);
    }

    @Test
    void throwsExceptionWhenDeleteByIdEntityNotFound() {
        // Given
        long id = 1L;
        when(customerDAO.existsById(id)).thenReturn(Boolean.FALSE);

        // When
        assertThatThrownBy(() -> underTest.deleteById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with passed id not found");

        // Then
        verify(customerDAO, never()).deleteById(id);
    }

    @Test
    void existsByEmail() {
        // Given
        final String email = "foo@gmail.com";
        when(customerDAO.existsByEmail(email)).thenReturn(anyBoolean());

        // When
        underTest.existsByEmail(email);

        // Then
        verify(customerDAO).existsByEmail(email);
    }

    @Test
    void existsById() {
        // Given
        long id = 1;
        when(customerDAO.existsById(id)).thenReturn(anyBoolean());

        // When
        underTest.existsById(id);

        // Then
        verify(customerDAO).existsById(id);
    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id, "Alex", 19, "alex@gmail.com"
        );
        when(customerDAO.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", 23, newEmail);

        when(customerDAO.existsByEmail(newEmail)).thenReturn(false);

        // When
        underTest.update(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).update(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id, "Alex", 19, "alex@gmail.com"
        );
        when(customerDAO.findById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", null, null);

        // When
        underTest.update(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).update(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id, "Alex", 19, "alex@gmail.com"
        );
        when(customerDAO.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, newEmail);

        when(customerDAO.existsByEmail(newEmail)).thenReturn(false);

        // When
        underTest.update(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).update(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id, "Alex", 19, "alex@gmail.com"
        );
        when(customerDAO.findById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, 22, null);

        // When
        underTest.update(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDAO).update(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void throwsExceptionWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id, "Alex", 19, "alex@gmail.com"
        );
        when(customerDAO.findById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, newEmail);

        when(customerDAO.existsByEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.update(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        // Then
        verify(customerDAO, never()).update(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        long id = 10;
        Customer customer = new Customer(
                id, "Alex", 19, "alex@gmail.com"
        );
        when(customerDAO.findById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getAge(), customer.getEmail());

        // When
        assertThatThrownBy(() -> underTest.update(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        // Then
        verify(customerDAO, never()).update(any());
    }
}