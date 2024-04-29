package com.generic.customer;

import com.generic.exception.DuplicateResourceException;
import com.generic.exception.RequestValidationException;
import com.generic.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(final CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer findById(final long id) {
        return customerDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with passed id [%s] not found".formatted(id)
                ));
    }

    public List<Customer> findAll() {
        return customerDAO.findAll();
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

    public boolean existsByEmail(final String email) {
        return customerDAO.existsByEmail(email);
    }

    public boolean existsById(final Long id) {
        return customerDAO.existsById(id);
    }

    public void update(final Long customerId, final CustomerUpdateRequest updateRequest) {
        final Customer customer = findById(customerId);

        boolean changes = false;

        String newName = null;
        String newEmail = null;
        Integer newAge = null;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.name())) {
            newName = updateRequest.name();
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.age())) {
            newAge = updateRequest.age();
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.email())) {
            if (customerDAO.existsByEmail(updateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            newEmail = updateRequest.email();
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("No data changes found");
        }

        Customer updated = new Customer(
                customer.id(),
                Objects.nonNull(newName) ? newName : customer.name(),
                Objects.nonNull(newAge) ? newAge : customer.age(),
                Objects.nonNull(newEmail) ? newEmail : customer.email()
        );

        customerDAO.update(updated);
    }
}
