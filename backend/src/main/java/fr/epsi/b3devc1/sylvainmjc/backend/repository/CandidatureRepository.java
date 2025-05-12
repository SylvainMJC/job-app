package fr.epsi.b3devc1.sylvainmjc.backend.repository;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
} 