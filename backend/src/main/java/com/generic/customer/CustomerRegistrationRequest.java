package com.generic.customer;

public record CustomerRegistrationRequest(
        String name,
        Integer age,
        String email
) {
}
