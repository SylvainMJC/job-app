package fr.epsi.b3devc1.sylvainmjc.backend.entity;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ActiveProfiles("test")
public class OffreTest {

    @Test
    void testOffreConstructor() {
        // Arrange
        Offre offre = new Offre();
        
        // Act
        offre.setId(1L);
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        LocalDateTime now = LocalDateTime.now();
        offre.setDateCreation(now);
        
        // Assert
        assertEquals(1L, offre.getId());
        assertEquals("Développeur Java", offre.getTitre());
        assertEquals("EPSI", offre.getEntreprise());
        assertEquals("Lyon", offre.getLocalisation());
        assertEquals("Description du poste", offre.getDescription());
        assertEquals(now, offre.getDateCreation());
    }
    
    @Test
    void testOffreEquality() {
        // Arrange
        Offre offre1 = new Offre();
        offre1.setId(1L);
        offre1.setTitre("Développeur Java");
        
        Offre offre2 = new Offre();
        offre2.setId(1L);
        offre2.setTitre("Développeur Java");
        
        Offre offre3 = new Offre();
        offre3.setId(2L);
        offre3.setTitre("Développeur Python");
        
        // Assert
        assertEquals(offre1.getId(), offre2.getId());
        assertNotEquals(offre1.getId(), offre3.getId());
    }
} 