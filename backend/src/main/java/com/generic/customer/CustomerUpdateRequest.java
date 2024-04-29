package com.generic.customer;

public record CustomerUpdateRequest(
        String name,
        Integer age,
        String email
) {
}
