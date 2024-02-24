package com.bardiniww;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainersTest {

    /**
     * Some useful commands to debug container:
     *
     * docker ps - to check container name and past it then
     * docker exec -it actualcontainername bash - to join inside container bash
     * psql -U bardiniww -d bardiniww-dao-unit test - to join the inside database
     *
     */
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("bardiniww-dao-unit-test")
                    .withUsername("bardiniww")
                    .withPassword("password");
    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }
}
