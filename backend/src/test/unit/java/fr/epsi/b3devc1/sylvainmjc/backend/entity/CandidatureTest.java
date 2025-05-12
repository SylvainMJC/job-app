package fr.epsi.b3devc1.sylvainmjc.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class CandidatureTest {

    private Candidature candidature;
    private Offre offre;
    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        offre = new Offre();
        offre.setId(1L);
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(now);

        candidature = new Candidature();
        candidature.setId(1L);
        candidature.setNom("Dupont");
        candidature.setPrenom("Jean");
        candidature.setEmail("jean.dupont@example.com");
        candidature.setTelephone("0123456789");
        candidature.setMessage("Je suis intéressé par ce poste");
        candidature.setDateCandidat(now);
        candidature.setOffre(offre);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, candidature.getId());
        assertEquals("Dupont", candidature.getNom());
        assertEquals("Jean", candidature.getPrenom());
        assertEquals("jean.dupont@example.com", candidature.getEmail());
        assertEquals("0123456789", candidature.getTelephone());
        assertEquals("Je suis intéressé par ce poste", candidature.getMessage());
        assertEquals(now, candidature.getDateCandidat());
        assertEquals(offre, candidature.getOffre());
    }

    @Test
    void testEqualsAndHashCode() {
        // Same object
        assertEquals(candidature, candidature);
        assertEquals(candidature.hashCode(), candidature.hashCode());

        // Different object with same ID but different fields
        // With @Data, equals compares all fields, not just ID
        Candidature candidature2 = new Candidature();
        candidature2.setId(1L);
        assertNotEquals(candidature, candidature2);
        
        // Copy of the first object with all fields the same
        Candidature candidature3 = new Candidature();
        candidature3.setId(1L);
        candidature3.setNom("Dupont");
        candidature3.setPrenom("Jean");
        candidature3.setEmail("jean.dupont@example.com");
        candidature3.setTelephone("0123456789");
        candidature3.setMessage("Je suis intéressé par ce poste");
        candidature3.setDateCandidat(now);
        candidature3.setOffre(offre);
        assertEquals(candidature, candidature3);
    }

    @Test
    void testToString() {
        String toString = candidature.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nom=Dupont"));
        assertTrue(toString.contains("prenom=Jean"));
        assertTrue(toString.contains("email=jean.dupont@example.com"));
        assertTrue(toString.contains("telephone=0123456789"));
        assertTrue(toString.contains("message=Je suis intéressé par ce poste"));
    }
    
    @Test
    void testOffreRelationship() {
        assertNotNull(candidature.getOffre());
        assertEquals(1L, candidature.getOffre().getId());
        assertEquals("Développeur Java", candidature.getOffre().getTitre());
        
        // Modifier l'offre
        Offre newOffre = new Offre();
        newOffre.setId(2L);
        newOffre.setTitre("Développeur Web");
        candidature.setOffre(newOffre);
        
        assertEquals(newOffre, candidature.getOffre());
        assertEquals(2L, candidature.getOffre().getId());
        assertEquals("Développeur Web", candidature.getOffre().getTitre());
    }
} 