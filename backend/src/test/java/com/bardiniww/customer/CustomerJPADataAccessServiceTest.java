package com.bardiniww.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.findAll();

        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        long id = 1;

        // When
        underTest.findById(id);

        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                1L, "Vasya", 12, "vasya@gmail.com"
        );

        // When
        underTest.save(customer);

        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        // Given
        String email = "foo@gmail.com";

        // When
        underTest.existsByEmail(email);

        // Then
        verify(customerRepository).existsByEmail(email);
    }

    @Test
    void existsCustomerById() {
        // Given
        long id = 1;

        // When
        underTest.existsById(id);

        // Then
        verify(customerRepository).existsById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
        long id = 1;

        // When
        underTest.deleteById(id);

        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                1L, "Vasya", 12, "vasya@gmail.com"
        );

        // When
        underTest.update(customer);

        // Then
        verify(customerRepository).save(customer);
    }
}