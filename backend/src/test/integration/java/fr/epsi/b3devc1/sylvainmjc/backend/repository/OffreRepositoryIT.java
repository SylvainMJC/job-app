package fr.epsi.b3devc1.sylvainmjc.backend.repository;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Tag("integration")
@ActiveProfiles("integration")
class OffreRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OffreRepository offreRepository;

    @Test
    void testSaveAndFindById() {
        // Créer et sauvegarder une offre
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());

        Offre savedOffre = entityManager.persistAndFlush(offre);
        
        // Vérifier que l'offre est bien retrouvée par son ID
        Optional<Offre> foundOffre = offreRepository.findById(savedOffre.getId());
        
        assertTrue(foundOffre.isPresent());
        assertEquals(savedOffre.getId(), foundOffre.get().getId());
        assertEquals(savedOffre.getTitre(), foundOffre.get().getTitre());
    }

    @Test
    void testFindAll() {
        // Créer et sauvegarder plusieurs offres
        Offre offre1 = new Offre();
        offre1.setTitre("Développeur Java");
        offre1.setEntreprise("EPSI");
        offre1.setLocalisation("Lyon");
        offre1.setDescription("Description du poste 1");
        offre1.setDateCreation(LocalDateTime.now());
        
        Offre offre2 = new Offre();
        offre2.setTitre("Développeur Web");
        offre2.setEntreprise("EPSI");
        offre2.setLocalisation("Paris");
        offre2.setDescription("Description du poste 2");
        offre2.setDateCreation(LocalDateTime.now());
        
        entityManager.persist(offre1);
        entityManager.persist(offre2);
        entityManager.flush();
        
        // Vérifier que toutes les offres sont récupérées
        List<Offre> offres = offreRepository.findAll();
        
        assertEquals(2, offres.size());
    }

    @Test
    void testDeleteById() {
        // Créer et sauvegarder une offre
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());

        Offre savedOffre = entityManager.persistAndFlush(offre);
        
        // Supprimer l'offre
        offreRepository.deleteById(savedOffre.getId());
        
        // Vérifier que l'offre a bien été supprimée
        Optional<Offre> deletedOffre = offreRepository.findById(savedOffre.getId());
        
        assertFalse(deletedOffre.isPresent());
    }

    @Test
    void testUpdate() {
        // Créer et sauvegarder une offre
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());

        Offre savedOffre = entityManager.persistAndFlush(offre);
        
        // Modifier l'offre
        savedOffre.setTitre("Développeur Java Senior");
        offreRepository.save(savedOffre);
        
        // Vérifier que les modifications ont été sauvegardées
        Optional<Offre> updatedOffre = offreRepository.findById(savedOffre.getId());
        
        assertTrue(updatedOffre.isPresent());
        assertEquals("Développeur Java Senior", updatedOffre.get().getTitre());
    }
} 