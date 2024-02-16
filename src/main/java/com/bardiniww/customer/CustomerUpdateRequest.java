package com.bardiniww.customer;

public record CustomerUpdateRequest(
        String name,
        Integer age,
        String email
) {
}
