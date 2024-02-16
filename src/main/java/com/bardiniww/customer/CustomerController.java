package com.bardiniww.customer;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("{customerId}")
    public Customer findById(@PathVariable("customerId") Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    public void register(@RequestBody CustomerRegistrationRequest customer) {
        customerService.register(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteById(@PathVariable("customerId") Long id) {
        customerService.deleteById(id);
    }

    @PutMapping("{customerId}")
    public void update(
            @PathVariable("customerId") Long customerId,
            @RequestBody CustomerUpdateRequest customerUpdateRequest
    ) {
        customerService.update(customerId, customerUpdateRequest);
    }
}
