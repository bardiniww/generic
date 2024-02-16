package com.bardiniww.customer;

import com.bardiniww.exception.DuplicateResourceException;
import com.bardiniww.exception.RequestValidationException;
import com.bardiniww.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jpa") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer findById(long id) {
        return customerDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with passed id [%s] not found".formatted(id)
                ));
    }

    public void register(final CustomerRegistrationRequest customer) {
        if (existsByEmail(customer.email())) {
            throw new DuplicateResourceException("Passed email already taken");
        }

        customerDAO.save(new Customer(
                customer.name(),
                customer.age(),
                customer.email()
        ));
    }

    public void deleteById(final Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Customer with passed id not found");
        }

        customerDAO.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return customerDAO.existsByEmail(email);
    }

    public boolean existsById(Long id) {
        return customerDAO.existsById(id);
    }

    public void update(final Long customerId, final CustomerUpdateRequest updateRequest) {
        final Customer customer = findById(customerId);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDAO.existsByEmail(updateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found");
        }

        customerDAO.update(customer);
    }
}
