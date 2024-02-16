package com.bardiniww.customer;

public record CustomerRegistrationRequest(
        String name,
        Integer age,
        String email
) {
}
