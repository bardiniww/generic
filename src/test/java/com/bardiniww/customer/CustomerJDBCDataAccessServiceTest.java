package com.bardiniww.customer;

import com.bardiniww.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        final List<Customer> result = underTest.findAll();

        // Then
        assertThat(result).isNotEmpty();
    }

    @Test
    void findById() {
//        // Given
//        Customer customer = new Customer(
//                FAKER.name().fullName(),
//                25,
//                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID()
//        );
//        underTest.save(customer);
//
//        // When
//        underTest.all
    }



    @Test
    void existsByEmail() {
        // Given

        // When

        // Then
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

        // When

        // Then
    }

    @Test
    void deleteById() {
        // Given

        // When

        // Then
    }

    @Test
    void update() {
        // Given

        // When

        // Then
    }
}