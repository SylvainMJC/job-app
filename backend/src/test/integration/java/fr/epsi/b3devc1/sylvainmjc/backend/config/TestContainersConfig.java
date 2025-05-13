package fr.epsi.b3devc1.sylvainmjc.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainersConfig {

    public static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(
                DockerImageName.parse("postgres:15-alpine")
                    .asCompatibleSubstituteFor("postgres"))
                .withDatabaseName("integration-tests-db")
                .withUsername("testuser")
                .withPassword("testpass")
                .withReuse(true);
        
        POSTGRES_CONTAINER.start();
    }

    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return POSTGRES_CONTAINER;
    }
    
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword(),
                // Configuration explicite pour les transactions
                "spring.datasource.hikari.auto-commit=false",
                "spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true"
            ).applyTo(applicationContext.getEnvironment());
        }
    }
} 