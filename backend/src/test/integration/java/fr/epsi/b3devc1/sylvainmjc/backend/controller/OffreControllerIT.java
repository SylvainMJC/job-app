package fr.epsi.b3devc1.sylvainmjc.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.OffreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@ActiveProfiles("integration")
@Testcontainers
public class OffreControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("integration-tests-db")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("init-db.sql");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        // Ensure container is started
        postgresContainer.start();
        System.out.println("PostgreSQL container started at: " + postgresContainer.getJdbcUrl());
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OffreRepository offreRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données avant chaque test
        offreRepository.deleteAll();
    }

    @Test
    void testGetAllOffres_WhenNoOffres_ShouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/offres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetAllOffres_WhenOffresExist_ShouldReturnOffres() throws Exception {
        // Arrange
        Offre offre1 = new Offre();
        offre1.setTitre("Développeur Java");
        offre1.setEntreprise("EPSI");
        offre1.setLocalisation("Lyon");
        offre1.setDescription("Description du poste");
        offre1.setDateCreation(LocalDateTime.now());
        offreRepository.save(offre1);

        Offre offre2 = new Offre();
        offre2.setTitre("Développeur Web");
        offre2.setEntreprise("EPSI");
        offre2.setLocalisation("Paris");
        offre2.setDescription("Description du poste 2");
        offre2.setDateCreation(LocalDateTime.now());
        offreRepository.save(offre2);

        // Act & Assert
        mockMvc.perform(get("/api/offres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titre", equalTo("Développeur Java")))
                .andExpect(jsonPath("$[1].titre", equalTo("Développeur Web")));
    }

    @Test
    void testGetOffreById_WhenOffreExists_ShouldReturnOffre() throws Exception {
        // Arrange
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());
        Offre savedOffre = offreRepository.save(offre);

        // Act & Assert
        mockMvc.perform(get("/api/offres/{id}", savedOffre.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(savedOffre.getId().intValue())))
                .andExpect(jsonPath("$.titre", equalTo("Développeur Java")));
    }

    @Test
    void testGetOffreById_WhenOffreDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/offres/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateOffre_ShouldCreateAndReturnOffre() throws Exception {
        // Arrange
        String offreJson = "{\"titre\":\"Développeur Java\",\"entreprise\":\"EPSI\",\"localisation\":\"Lyon\",\"description\":\"Description du poste\"}";

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/offres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(offreJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre", equalTo("Développeur Java")))
                .andExpect(jsonPath("$.entreprise", equalTo("EPSI")))
                .andExpect(jsonPath("$.localisation", equalTo("Lyon")))
                .andExpect(jsonPath("$.description", equalTo("Description du poste")))
                .andReturn();

        // Récupérer l'ID de l'offre créée
        String contentAsString = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);
        int offreId = jsonNode.get("id").asInt();
        
        // Vérifier que l'offre a bien été créée en base
        mockMvc.perform(get("/api/offres/{id}", offreId))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateOffre_WhenOffreExists_ShouldUpdateAndReturnOffre() throws Exception {
        // Arrange
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());
        Offre savedOffre = offreRepository.save(offre);

        String updatedOffreJson = "{\"titre\":\"Développeur Java Senior\",\"entreprise\":\"EPSI\",\"localisation\":\"Lyon\",\"description\":\"Description mise à jour\"}";

        // Act & Assert
        mockMvc.perform(put("/api/offres/{id}", savedOffre.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedOffreJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(savedOffre.getId().intValue())))
                .andExpect(jsonPath("$.titre", equalTo("Développeur Java Senior")))
                .andExpect(jsonPath("$.description", equalTo("Description mise à jour")));

        // Vérifier que l'offre a bien été mise à jour en base
        mockMvc.perform(get("/api/offres/{id}", savedOffre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre", equalTo("Développeur Java Senior")));
    }

    @Test
    void testUpdateOffre_WhenOffreDoesNotExist_ShouldReturnNotFound() throws Exception {
        String offreJson = "{\"titre\":\"Développeur Java\",\"entreprise\":\"EPSI\",\"localisation\":\"Lyon\",\"description\":\"Description du poste\"}";

        mockMvc.perform(put("/api/offres/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(offreJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteOffre_WhenOffreExists_ShouldDeleteOffre() throws Exception {
        // Arrange
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());
        Offre savedOffre = offreRepository.save(offre);

        // Act & Assert
        mockMvc.perform(delete("/api/offres/{id}", savedOffre.getId()))
                .andExpect(status().isOk());

        // Vérifier que l'offre a bien été supprimée
        mockMvc.perform(get("/api/offres/{id}", savedOffre.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteOffre_WhenOffreDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/offres/999"))
                .andExpect(status().isNotFound());
    }
} 