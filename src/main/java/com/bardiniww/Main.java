package com.bardiniww;

import com.bardiniww.customer.Customer;
import com.bardiniww.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository repository) {
        return args -> {
            Customer dummy1 = new Customer(
                    "Ivan",
                    20,
                    "ivan@mail"
            );
            Customer dummy2 = new Customer(
                    "Mike",
                    20,
                    "mike@mail"
            );

            repository.saveAll(List.of(dummy1, dummy2));
        };
    }
}
