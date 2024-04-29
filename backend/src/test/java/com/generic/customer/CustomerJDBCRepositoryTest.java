package com.generic.customer;

import com.generic.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCRepositoryTest extends AbstractTestcontainers {

    private CustomerJDBCRepository underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCRepository(
          jdbcTemplate(),
          customerRowMapper
        );
    }

    @Test
    void findAll() {
        // Given
        Customer customer = new Customer(
                null,
                FAKER.name().fullName(),
                25,
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID()
        );
        underTest.save(customer);

        // When
        final List<Customer> actual = underTest.findAll();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void findById() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );
        underTest.save(customer);
        final long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .findFirst()
                .map(Customer::id)
                .orElseThrow();

        // When
        final Optional<Customer> actual = underTest.findById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
           assertThat(c.id()).isEqualTo(id);
           assertThat(c.name()).isEqualTo(customer.name());
           assertThat(c.age()).isEqualTo(customer.age());
           assertThat(c.email()).isEqualTo(customer.email());
        });

    }

    @Test
    void willReturnEmptyWhenFindById() {
        // Given
        long id = 0;

        // When
        var actual = underTest.findById(id);

        // Then
        assertThat(actual).isEmpty();
    }



    @Test
    void existsByEmail() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        // When
        boolean actual = underTest.existsByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When
        boolean actual = underTest.existsByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void save() {
        // Given

        // When

        // Then
    }

    @Test
    void existsById() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .map(Customer::id)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existsById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdNotPresent() {
        // Given
        long id = -1;

        // When
        var actual = underTest.existsById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteById() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .map(Customer::id)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteById(id);

        // Then
        Optional<Customer> actual = underTest.findById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .map(Customer::id)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When name changed
        Customer update = new Customer(
                id,
                newName,
                customer.age(),
                customer.email()
        );

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.id()).isEqualTo(id);
            assertThat(c.name()).isEqualTo(newName); // change
            assertThat(c.email()).isEqualTo(customer.email());
            assertThat(c.age()).isEqualTo(customer.age());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .map(Customer::id)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When name changed
        Customer update = new Customer(
                id,
                customer.name(),
                customer.age(),
                newEmail
        );

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.id()).isEqualTo(id);
            assertThat(c.name()).isEqualTo(customer.name());
            assertThat(c.email()).isEqualTo(newEmail); // change
            assertThat(c.age()).isEqualTo(customer.age());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .map(Customer::id)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        // When age changed
        Customer update = new Customer(
                id,
                customer.name(),
                newAge,
                customer.email()
        );

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.id()).isEqualTo(id);
            assertThat(c.name()).isEqualTo(customer.name());
            assertThat(c.email()).isEqualTo(customer.email());
            assertThat(c.age()).isEqualTo(newAge); // change
        });
    }

    @Test
    void willUpdateAllPropertyCustomer() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .map(Customer::id)
                .findFirst()
                .orElseThrow();

        String newEmail = "newemail@newemail.com";

        // When update with new name, age and email
        Customer update = new Customer(
                id,
                "foo",
                22,
                newEmail
        );

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
            assertThat(updated.id()).isEqualTo(id);
            assertThat(updated.name()).isEqualTo("foo");
            assertThat(updated.email()).isEqualTo(newEmail);
            assertThat(updated.age()).isEqualTo(22);
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        final String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                25,
                email
        );

        underTest.save(customer);

        long id = underTest.findAll()
                .stream()
                .filter(c -> c.email().equals(email))
                .map(Customer::id)
                .findFirst()
                .orElseThrow();

        // When update without no changes
        underTest.update(customer);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.id()).isEqualTo(id);
            assertThat(c.age()).isEqualTo(customer.age());
            assertThat(c.name()).isEqualTo(customer.name());
            assertThat(c.email()).isEqualTo(customer.email());
        });
    }
}