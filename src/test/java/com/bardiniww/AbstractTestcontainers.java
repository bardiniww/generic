package com.bardiniww;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestcontainers {

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
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("bardiniww-dao-unit-test")
                    .withUsername("bardiniww")
                    .withPassword("password");

    /**
     * Mapping application datasource properties (application.yml) inside test environment
     */
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgreSQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgreSQLContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgreSQLContainer::getPassword
        );
    }

//    in case to check that everything works
//    @Test
//    void canStartPostgresDB() {
//        assertThat(postgreSQLContainer.isCreated()).isTrue();
//        assertThat(postgreSQLContainer.isRunning()).isTrue();
//    }
//
//    @Test
//    void canApplyDbMigrationsWithFlyway() {
//        // https://documentation.red-gate.com/flyway/flyway-cli-and-api/getting-started
//        Flyway flyway = Flyway
//                .configure()
//                .dataSource(
//                        postgreSQLContainer.getJdbcUrl(),
//                        postgreSQLContainer.getUsername(),
//                        postgreSQLContainer.getPassword()
//                )
//                .load();
//        flyway.migrate();
//        System.out.println();
//    }
}
