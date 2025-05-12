package fr.epsi.b3devc1.sylvainmjc.backend.repository;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Candidature;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("integration")
class CandidatureRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CandidatureRepository candidatureRepository;
    
    @Autowired
    private OffreRepository offreRepository;

    @Test
    void testFindByOffre() {
        // Créer et sauvegarder des offres
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

        final Offre savedOffre1 = entityManager.persist(offre1);
        final Offre savedOffre2 = entityManager.persist(offre2);

        // Créer et sauvegarder des candidatures
        Candidature candidature1 = new Candidature();
        candidature1.setNom("Dupont");
        candidature1.setPrenom("Jean");
        candidature1.setEmail("jean.dupont@example.com");
        candidature1.setTelephone("0123456789");
        candidature1.setMessage("Je suis intéressé par ce poste");
        candidature1.setDateCandidat(LocalDateTime.now());
        candidature1.setOffre(savedOffre1);

        Candidature candidature2 = new Candidature();
        candidature2.setNom("Martin");
        candidature2.setPrenom("Sophie");
        candidature2.setEmail("sophie.martin@example.com");
        candidature2.setTelephone("9876543210");
        candidature2.setMessage("Je suis intéressée par ce poste");
        candidature2.setDateCandidat(LocalDateTime.now());
        candidature2.setOffre(savedOffre1);

        Candidature candidature3 = new Candidature();
        candidature3.setNom("Durand");
        candidature3.setPrenom("Pierre");
        candidature3.setEmail("pierre.durand@example.com");
        candidature3.setTelephone("5678901234");
        candidature3.setMessage("Je suis intéressé par ce poste");
        candidature3.setDateCandidat(LocalDateTime.now());
        candidature3.setOffre(savedOffre2);

        entityManager.persist(candidature1);
        entityManager.persist(candidature2);
        entityManager.persist(candidature3);
        entityManager.flush();

        // Vérifier que les candidatures sont bien récupérées par offre
        List<Candidature> candidaturesOffre1 = candidatureRepository.findByOffre(savedOffre1);
        List<Candidature> candidaturesOffre2 = candidatureRepository.findByOffre(savedOffre2);

        assertEquals(2, candidaturesOffre1.size());
        assertEquals(1, candidaturesOffre2.size());

        // Vérifier que les candidatures récupérées sont bien associées à la bonne offre
        final Long offre1Id = savedOffre1.getId();
        final Long offre2Id = savedOffre2.getId();
        
        candidaturesOffre1.forEach(candidature -> 
            assertEquals(offre1Id, candidature.getOffre().getId())
        );
        
        candidaturesOffre2.forEach(candidature -> 
            assertEquals(offre2Id, candidature.getOffre().getId())
        );
    }

    @Test
    void testSaveAndFindById() {
        // Créer et sauvegarder une offre
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());
        offre = entityManager.persist(offre);

        // Créer et sauvegarder une candidature
        Candidature candidature = new Candidature();
        candidature.setNom("Dupont");
        candidature.setPrenom("Jean");
        candidature.setEmail("jean.dupont@example.com");
        candidature.setTelephone("0123456789");
        candidature.setMessage("Je suis intéressé par ce poste");
        candidature.setDateCandidat(LocalDateTime.now());
        candidature.setOffre(offre);

        Candidature savedCandidature = entityManager.persistAndFlush(candidature);

        // Vérifier que la candidature est bien sauvegardée
        Candidature foundCandidature = candidatureRepository.findById(savedCandidature.getId()).get();
        assertEquals(savedCandidature.getId(), foundCandidature.getId());
        assertEquals(savedCandidature.getNom(), foundCandidature.getNom());
        assertEquals(savedCandidature.getPrenom(), foundCandidature.getPrenom());
        assertEquals(savedCandidature.getOffre().getId(), foundCandidature.getOffre().getId());
    }

    @Test
    void testCascadeDeleteOffre() {
        // Créer et sauvegarder une offre
        Offre offre = new Offre();
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());
        
        // Créer et lier des candidatures
        Candidature candidature1 = new Candidature();
        candidature1.setNom("Dupont");
        candidature1.setPrenom("Jean");
        candidature1.setEmail("jean.dupont@example.com");
        candidature1.setTelephone("0123456789");
        candidature1.setMessage("Je suis intéressé par ce poste");
        candidature1.setDateCandidat(LocalDateTime.now());
        
        Candidature candidature2 = new Candidature();
        candidature2.setNom("Martin");
        candidature2.setPrenom("Sophie");
        candidature2.setEmail("sophie.martin@example.com");
        candidature2.setTelephone("9876543210");
        candidature2.setMessage("Je suis intéressée par ce poste");
        candidature2.setDateCandidat(LocalDateTime.now());
        
        // Maintenant établir la relation bidirectionnelle
        candidature1.setOffre(offre);
        candidature2.setOffre(offre);
        offre.getCandidatures().add(candidature1);
        offre.getCandidatures().add(candidature2);
        
        // Sauvegarder l'offre avec les candidatures
        offre = offreRepository.save(offre);
        entityManager.flush();
        entityManager.clear();
        
        // Vérifier qu'il y a bien 2 candidatures
        List<Candidature> candidatures = candidatureRepository.findByOffre(offre);
        assertEquals(2, candidatures.size());
        
        // Récupérer l'ID de l'offre avant de la supprimer
        Long offreId = offre.getId();
        
        // Supprimer l'offre (devrait supprimer les candidatures grâce à cascade=ALL)
        offreRepository.deleteById(offreId);
        entityManager.flush();
        entityManager.clear();
        
        // Vérifier que l'offre n'existe plus
        assertFalse(offreRepository.existsById(offreId));
        
        // Vérifier que les candidatures ont été supprimées (grâce à la cascade)
        List<Candidature> allCandidatures = candidatureRepository.findAll();
        assertEquals(0, allCandidatures.size());
    }
} 