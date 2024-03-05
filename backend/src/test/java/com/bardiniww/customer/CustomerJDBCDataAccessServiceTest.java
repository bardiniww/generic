package com.bardiniww.customer;

import com.bardiniww.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
          jdbcTemplate(),
          customerRowMapper
        );
    }

    @Test
    void findAll() {
        // Given
        Customer customer = new Customer(
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
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();

        // When
        final Optional<Customer> asctual = underTest.findById(id);

        // Then
        assertThat(asctual).isPresent().hasValueSatisfying(c -> {
           assertThat(c.getId()).isEqualTo(id);
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getAge()).isEqualTo(customer.getAge());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
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
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
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
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
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
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When name changed
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); // change
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
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
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When name changed
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail); // change
            assertThat(c.getAge()).isEqualTo(customer.getAge());
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
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        // When age changed
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge); // change
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
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When update with new name, age and email
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        String newEmail = UUID.randomUUID().toString();
        update.setEmail(newEmail);
        update.setAge(22);

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
            assertThat(updated.getId()).isEqualTo(id);
            assertThat(updated.getName()).isEqualTo("foo");
            assertThat(updated.getEmail()).isEqualTo(newEmail);
            assertThat(updated.getAge()).isEqualTo(22);
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
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When update without no changes
        Customer update = new Customer();
        update.setId(id);

        underTest.update(update);

        // Then
        Optional<Customer> actual = underTest.findById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }
}