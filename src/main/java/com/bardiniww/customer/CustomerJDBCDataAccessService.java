package com.bardiniww.customer;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO {
    @Override
    public Optional<Customer> findById(final Long id) {
        return null;
    }

    @Override
    public boolean existsByEmail(final String email) {
        return false;
    }

    @Override
    public void save(final Customer customer) {

    }

    @Override
    public boolean existsById(final Long id) {
        return false;
    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public void update(final Customer customer) {

    }
}
