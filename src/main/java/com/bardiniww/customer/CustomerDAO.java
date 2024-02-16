package com.bardiniww.customer;

import java.util.Optional;

public interface CustomerDAO {
    Optional<Customer> findById(final Long id);
    boolean existsByEmail(String email);
    void save(Customer customer);
    boolean existsById(Long id);
    void deleteById(Long id);
    void update(Customer customer);
}
