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

        // Different object with same ID
        Offre offre2 = new Offre();
        offre2.setId(1L);
        assertEquals(offre, offre2); // Lombok's @Data generates equals based on all fields
        
        // Different object with different ID
        Offre offre3 = new Offre();
        offre3.setId(2L);
        assertNotEquals(offre, offre3);
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