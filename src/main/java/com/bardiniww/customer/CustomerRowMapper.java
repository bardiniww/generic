package com.bardiniww.customer;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new Customer(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("email")
        );
    }
}
