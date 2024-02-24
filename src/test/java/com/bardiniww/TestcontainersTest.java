package com.bardiniww;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainersTest {

    /**
     * Some useful commands to debug the container:
     *
     * docker ps - to check container name and past it then
     * docker exec -it actualcontainername bash - to join inside container bash
     * psql -U bardiniww -d bardiniww-dao-unit test - to connect psql
     * \c bardiniww-dao-unit-test - connect to the database
     * \dt - describe tables
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

    @Test
    void canApplyDbMigrationsWithFlyway() {
        // https://documentation.red-gate.com/flyway/flyway-cli-and-api/getting-started
        Flyway flyway = Flyway
                .configure()
                .dataSource(
                        postgreSQLContainer.getJdbcUrl(),
                        postgreSQLContainer.getUsername(),
                        postgreSQLContainer.getPassword()
                )
                .load();
        flyway.migrate();
        System.out.println();
    }
}
