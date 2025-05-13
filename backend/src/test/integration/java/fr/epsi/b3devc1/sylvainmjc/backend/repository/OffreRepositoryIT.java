package fr.epsi.b3devc1.sylvainmjc.backend.repository;

import fr.epsi.b3devc1.sylvainmjc.backend.config.PostgresTestContainer;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
@ActiveProfiles("integration")
@Testcontainers
public class OffreRepositoryIT {

    private static final PostgreSQLContainer<?> postgresContainer = PostgresTestContainer.getInstance();

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private OffreRepository offreRepository;

    @Test
    void testSaveAndFindById() {
        // Arrange
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());

        // Act
        Offre savedOffre = offreRepository.save(offre);
        Optional<Offre> foundOffre = offreRepository.findById(savedOffre.getId());

        // Assert
        assertTrue(foundOffre.isPresent());
        assertEquals("Développeur Java", foundOffre.get().getTitre());
        assertEquals("EPSI", foundOffre.get().getEntreprise());
    }

    @Test
    void testFindAll() {
        // Arrange
        offreRepository.deleteAll(); // Ensure clean state
        
        Offre offre1 = new Offre();
        offre1.setTitre("Développeur Java");
        offre1.setEntreprise("EPSI");
        
        Offre offre2 = new Offre();
        offre2.setTitre("Développeur Python");
        offre2.setEntreprise("EPSI");
        
        offreRepository.save(offre1);
        offreRepository.save(offre2);

        // Act
        List<Offre> offres = offreRepository.findAll();

        // Assert
        assertEquals(2, offres.size());
    }
} 