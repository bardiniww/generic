package com.bardiniww.customer;


import jakarta.persistence.*;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                )
        }
)
public class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private Long id;
    @Column(
            nullable = false
    )
    private String name;
    @Column(
            nullable = false
    )
    private Integer age;
    @Column(
            nullable = false
    )
    private String email;

    public Customer() {
    }

    public Customer(final String name, final Integer age, final String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    void setName(final String name) {
        this.name = name;
    }

    void setAge(final Integer age) {
        this.age = age;
    }

    void setEmail(final String email) {
        this.email = email;
    }
}
