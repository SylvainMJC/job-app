package fr.epsi.b3devc1.sylvainmjc.backend.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer {
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("integration-tests-db")
                .withUsername("testuser")
                .withPassword("testpass");
        POSTGRES_CONTAINER.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return POSTGRES_CONTAINER;
    }

    private PostgresTestContainer() {
        // Private constructor to prevent instantiation
    }
} 