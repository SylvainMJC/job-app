package fr.epsi.b3devc1.sylvainmjc.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

@TestConfiguration
public class TestContainersConfig {

    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = 
        new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("testuser")
            .withPassword("testpass")
            .withReuse(true);
    
    static {
        POSTGRES_CONTAINER.start();
        
        // Ensure clean shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (POSTGRES_CONTAINER.isRunning()) {
                System.out.println("Stopping PostgreSQL container...");
                POSTGRES_CONTAINER.stop();
            }
        }));
    }
    
    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        return POSTGRES_CONTAINER;
    }
    
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        Startables.deepStart(POSTGRES_CONTAINER).join();
        
        registry.add("spring.datasource.url", () -> 
            POSTGRES_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username", () -> 
            POSTGRES_CONTAINER.getUsername());
        registry.add("spring.datasource.password", () -> 
            POSTGRES_CONTAINER.getPassword());
    }
} 