package fr.epsi.b3devc1.sylvainmjc.backend.repository;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Candidature;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByOffre(Offre offre);
} 