package com.generic;

import com.generic.customer.Customer;
import com.generic.customer.CustomerJDBCRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

// use this to debug
    @Bean
    CommandLineRunner runner(CustomerJDBCRepository repository) {
        return args -> {
            var faker = new Faker();
            final Random random = new Random();
            final Name name = faker.name();
            final String firstName = name.firstName();
            final String lastName = name.lastName();
            final Customer customer = new Customer(
                    firstName +  " " + lastName,
                    random.nextInt(16, 99),
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@bardincode.com"
            );

            repository.save(customer);
        };
    }
}
