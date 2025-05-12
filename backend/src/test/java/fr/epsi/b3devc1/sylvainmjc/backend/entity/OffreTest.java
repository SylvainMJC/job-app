package fr.epsi.b3devc1.sylvainmjc.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OffreTest {

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
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, offre.getId());
        assertEquals("Développeur Java", offre.getTitre());
        assertEquals("EPSI", offre.getEntreprise());
        assertEquals("Lyon", offre.getLocalisation());
        assertEquals("Description du poste", offre.getDescription());
        assertEquals(now, offre.getDateCreation());
    }

    @Test
    void testEqualsAndHashCode() {
        // Same object
        assertEquals(offre, offre);
        assertEquals(offre.hashCode(), offre.hashCode());

        // Different object with same ID but different fields
        // With @Data, equals compares all fields, not just ID
        Offre offre2 = new Offre();
        offre2.setId(1L);
        assertNotEquals(offre, offre2);
        
        // Copy of the first object with all fields the same
        Offre offre3 = new Offre();
        offre3.setId(1L);
        offre3.setTitre("Développeur Java");
        offre3.setEntreprise("EPSI");
        offre3.setLocalisation("Lyon");
        offre3.setDescription("Description du poste");
        offre3.setDateCreation(now);
        assertEquals(offre, offre3);
    }

    @Test
    void testToString() {
        String toString = offre.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("titre=Développeur Java"));
        assertTrue(toString.contains("entreprise=EPSI"));
        assertTrue(toString.contains("localisation=Lyon"));
        assertTrue(toString.contains("description=Description du poste"));
    }
} 