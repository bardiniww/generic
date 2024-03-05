package com.bardiniww.customer;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO {

    private static final String SQL_SELECT_BY_ID = "SELECT id, name, age, email FROM customer WHERE id = :id";
    private static final String SQL_SELECT_ALL = "SELECT id, name, age, email FROM customer";
    private static final String SQL_SELECT_COUNT_BY_ID = "SELECT COUNT(id) FROM customer WHERE id = :id";
    private static final String SQL_SELECT_COUNT_BY_EMAIL = "SELECT COUNT(id) FROM customer WHERE email = :email";
    private static final String SQL_INSERT = "INSERT INTO customer(name, age, email) VALUES (:name, :age, :email)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM customer WHERE id = :id";
    private static final String SQL_UPDATE_NAME_BY_ID = "UPDATE customer SET name = :name WHERE id = :id";
    private static final String SQL_UPDATE_AGE_BY_ID = "UPDATE customer SET age = :age WHERE id = :id";
    private static final String SQL_UPDATE_EMAIL_BY_ID = "UPDATE customer SET email = :email WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final CustomerRowMapper rowMapper;

    public CustomerJDBCDataAccessService(final NamedParameterJdbcTemplate jdbcTemplate, final CustomerRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<Customer> findById(final Long id) {
        return jdbcTemplate.query(
                        SQL_SELECT_BY_ID,
                        new MapSqlParameterSource()
                                .addValue("id", id),
                        rowMapper
                )
                .stream().findFirst();
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(
                        SQL_SELECT_ALL,
                        rowMapper
                );
    }

    @Override
    public boolean existsByEmail(final String email) {
        final Integer count = jdbcTemplate.queryForObject(
                SQL_SELECT_COUNT_BY_EMAIL,
                new MapSqlParameterSource()
                        .addValue("email", email),
                Integer.class
        );
        return count != null && count > 0;
    }

    @Override
    public void save(final Customer customer) {
        jdbcTemplate.update(
                SQL_INSERT,
                new MapSqlParameterSource()
                        .addValue("name", customer.getName())
                        .addValue("age", customer.getAge())
                        .addValue("email", customer.getEmail())
        );
    }

    @Override
    public boolean existsById(final Long id) {
        final Integer count = jdbcTemplate.queryForObject(
                SQL_SELECT_COUNT_BY_ID,
                new MapSqlParameterSource()
                        .addValue("id", id),
                Integer.class
        );
        return count != null && count > 0;
    }

    @Override
    public void deleteById(final Long id) {
        jdbcTemplate.update(
                SQL_DELETE_BY_ID,
                new MapSqlParameterSource()
                        .addValue("id", id)
        );
    }

    @Override
    public void update(final Customer customer) {
        if (Objects.nonNull(customer.getName())) {
            jdbcTemplate.update(
                    SQL_UPDATE_NAME_BY_ID,
                    new MapSqlParameterSource()
                            .addValue("name", customer.getName())
                            .addValue("id", customer.getId())
            );
        }
        if (Objects.nonNull(customer.getAge())) {
            jdbcTemplate.update(
                    SQL_UPDATE_AGE_BY_ID,
                    new MapSqlParameterSource()
                            .addValue("age", customer.getAge())
                            .addValue("id", customer.getId())
            );
        }
        if (Objects.nonNull(customer.getEmail())) {
            jdbcTemplate.update(
                    SQL_UPDATE_EMAIL_BY_ID,
                    new MapSqlParameterSource()
                            .addValue("email", customer.getEmail())
                            .addValue("id", customer.getId())
            );
        }
    }
}
