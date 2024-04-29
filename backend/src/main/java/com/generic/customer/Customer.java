package com.generic.customer;


import jakarta.persistence.*;


public record Customer(
        Long id,
        String name,
        Integer age,
        String email
) {
    public Customer(String name, Integer age, String email) {
        this(null, name, age, email);
    }
}
