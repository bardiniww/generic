package com.bardiniww.customer;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDAO {

    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findById(final Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return customerRepository.existsCustomersByEmail(email);
    }

    @Override
    public void save(final Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean existsById(final Long id) {
        return customerRepository.existsCustomersById(id);
    }

    @Override
    public void deleteById(final Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void update(final Customer customer) {
        customerRepository.save(customer);
    }
}
