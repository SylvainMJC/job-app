package fr.epsi.b3devc1.sylvainmjc.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.epsi.b3devc1.sylvainmjc.backend.config.TestContainersConfig;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Candidature;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.CandidatureRepository;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.OffreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@ActiveProfiles("integration")
@ContextConfiguration(initializers = TestContainersConfig.Initializer.class)
@Import(TestContainersConfig.class)
@Transactional
@TestPropertySource(properties = {
    "spring.jpa.properties.hibernate.connection.provider_disables_autocommit=true",
    "spring.datasource.hikari.auto-commit=false"
})
public class CandidatureControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private OffreRepository offreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Offre testOffre;

    @BeforeEach
    void setUp() {
        // Nettoyer la base de données avant chaque test
        candidatureRepository.deleteAll();
        offreRepository.deleteAll();
        
        // Vider le cache et synchroniser
        candidatureRepository.flush();
        offreRepository.flush();

        // Créer une offre de test
        testOffre = new Offre();
        testOffre.setTitre("Développeur Java");
        testOffre.setEntreprise("EPSI");
        testOffre.setLocalisation("Lyon");
        testOffre.setDescription("Description du poste");
        testOffre.setDateCreation(LocalDateTime.now());
        testOffre = offreRepository.saveAndFlush(testOffre);
    }

    @Test
    void testGetAllCandidatures_WhenNoCandidatures_ShouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/candidatures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetAllCandidatures_WhenCandidaturesExist_ShouldReturnCandidatures() throws Exception {
        // Arrange
        Candidature candidature1 = createCandidature("Dupont", "Jean", "jean.dupont@example.com");
        Candidature candidature2 = createCandidature("Martin", "Sophie", "sophie.martin@example.com");
        
        candidatureRepository.flush();

        // Act & Assert
        mockMvc.perform(get("/api/candidatures")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom", equalTo("Dupont")))
                .andExpect(jsonPath("$[1].nom", equalTo("Martin")));
    }

    @Test
    void testGetCandidatureById_WhenCandidatureExists_ShouldReturnCandidature() throws Exception {
        // Arrange
        Candidature candidature = createCandidature("Dupont", "Jean", "jean.dupont@example.com");
        candidatureRepository.flush();

        // Act & Assert
        mockMvc.perform(get("/api/candidatures/{id}", candidature.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(candidature.getId().intValue())))
                .andExpect(jsonPath("$.nom", equalTo("Dupont")))
                .andExpect(jsonPath("$.prenom", equalTo("Jean")));
    }

    @Test
    void testGetCandidatureById_WhenCandidatureDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/candidatures/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCandidaturesByOffre_WhenOffreExists_ShouldReturnCandidatures() throws Exception {
        // Arrange
        Candidature candidature1 = createCandidature("Dupont", "Jean", "jean.dupont@example.com");
        Candidature candidature2 = createCandidature("Martin", "Sophie", "sophie.martin@example.com");
        candidatureRepository.flush();

        // Act & Assert
        mockMvc.perform(get("/api/candidatures/offre/{offreId}", testOffre.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom", equalTo("Dupont")))
                .andExpect(jsonPath("$[1].nom", equalTo("Martin")));
    }

    @Test
    void testGetCandidaturesByOffre_WhenOffreDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/candidatures/offre/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCandidature_ShouldCreateAndReturnCandidature() throws Exception {
        // Arrange
        String candidatureJson = "{\"nom\":\"Dupont\",\"prenom\":\"Jean\",\"email\":\"jean.dupont@example.com\",\"telephone\":\"0123456789\",\"message\":\"Je suis intéressé par ce poste\"}";

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/candidatures")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidatureJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom", equalTo("Dupont")))
                .andExpect(jsonPath("$.prenom", equalTo("Jean")))
                .andExpect(jsonPath("$.email", equalTo("jean.dupont@example.com")))
                .andReturn();

        // Récupérer l'ID de la candidature créée
        String contentAsString = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);
        int candidatureId = jsonNode.get("id").asInt();

        // Vérifier que la candidature a bien été créée en base
        mockMvc.perform(get("/api/candidatures/{id}", candidatureId))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCandidatureForOffre_ShouldCreateAndReturnCandidature() throws Exception {
        // Arrange
        String candidatureJson = "{\"nom\":\"Dupont\",\"prenom\":\"Jean\",\"email\":\"jean.dupont@example.com\",\"telephone\":\"0123456789\",\"message\":\"Je suis intéressé par ce poste\"}";

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/api/candidatures/offre/{offreId}", testOffre.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidatureJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom", equalTo("Dupont")))
                .andExpect(jsonPath("$.prenom", equalTo("Jean")))
                .andExpect(jsonPath("$.email", equalTo("jean.dupont@example.com")))
                .andReturn();

        // Récupérer l'ID de la candidature créée
        String contentAsString = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(contentAsString);
        int candidatureId = jsonNode.get("id").asInt();

        // Vérifier que la candidature a bien été créée en base et est associée à l'offre
        Candidature candidature = candidatureRepository.findById((long) candidatureId).orElseThrow();
        assertEquals(testOffre.getId(), candidature.getOffre().getId());
    }

    @Test
    void testCreateCandidatureForOffre_WhenOffreDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Arrange
        String candidatureJson = "{\"nom\":\"Dupont\",\"prenom\":\"Jean\",\"email\":\"jean.dupont@example.com\",\"telephone\":\"0123456789\",\"message\":\"Je suis intéressé par ce poste\"}";

        // Act & Assert
        mockMvc.perform(post("/api/candidatures/offre/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidatureJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCandidature_WhenCandidatureExists_ShouldUpdateAndReturnCandidature() throws Exception {
        // Arrange
        Candidature candidature = createCandidature("Dupont", "Jean", "jean.dupont@example.com");
        candidatureRepository.flush();

        String updatedCandidatureJson = "{\"nom\":\"Dupont\",\"prenom\":\"Jean-Pierre\",\"email\":\"jean.pierre.dupont@example.com\",\"telephone\":\"0123456789\",\"message\":\"Je suis très intéressé par ce poste\"}";

        // Act & Assert
        mockMvc.perform(put("/api/candidatures/{id}", candidature.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedCandidatureJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(candidature.getId().intValue())))
                .andExpect(jsonPath("$.prenom", equalTo("Jean-Pierre")))
                .andExpect(jsonPath("$.email", equalTo("jean.pierre.dupont@example.com")));

        // Vérifier que la candidature a bien été mise à jour en base
        mockMvc.perform(get("/api/candidatures/{id}", candidature.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenom", equalTo("Jean-Pierre")));
    }

    @Test
    void testUpdateCandidature_WhenCandidatureDoesNotExist_ShouldReturnNotFound() throws Exception {
        String candidatureJson = "{\"nom\":\"Dupont\",\"prenom\":\"Jean\",\"email\":\"jean.dupont@example.com\",\"telephone\":\"0123456789\",\"message\":\"Je suis intéressé par ce poste\"}";

        mockMvc.perform(put("/api/candidatures/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidatureJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCandidature_WhenCandidatureExists_ShouldDeleteCandidature() throws Exception {
        // Arrange
        Candidature candidature = createCandidature("Dupont", "Jean", "jean.dupont@example.com");
        candidatureRepository.flush();

        // Act & Assert
        mockMvc.perform(delete("/api/candidatures/{id}", candidature.getId()))
                .andExpect(status().isOk());

        // Vérifier que la candidature a bien été supprimée
        mockMvc.perform(get("/api/candidatures/{id}", candidature.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCandidature_WhenCandidatureDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/candidatures/999"))
                .andExpect(status().isNotFound());
    }

    // Méthode utilitaire pour créer une candidature
    private Candidature createCandidature(String nom, String prenom, String email) {
        Candidature candidature = new Candidature();
        candidature.setNom(nom);
        candidature.setPrenom(prenom);
        candidature.setEmail(email);
        candidature.setTelephone("0123456789");
        candidature.setMessage("Je suis intéressé par ce poste");
        candidature.setDateCandidat(LocalDateTime.now());
        candidature.setOffre(testOffre);
        return candidatureRepository.save(candidature);
    }
} 