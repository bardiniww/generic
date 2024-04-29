package com.generic.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    Optional<Customer> findById(final Long id);
    List<Customer> findAll();
    boolean existsByEmail(String email);
    void save(Customer customer);
    boolean existsById(Long id);
    void deleteById(Long id);
    void update(Customer customer);
}
