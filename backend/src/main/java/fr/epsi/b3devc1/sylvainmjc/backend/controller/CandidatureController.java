package fr.epsi.b3devc1.sylvainmjc.backend.controller;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Candidature;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.CandidatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {

    @Autowired
    private CandidatureRepository candidatureRepository;

    @GetMapping
    public List<Candidature> getAllCandidatures() {
        return candidatureRepository.findAll();
    }

    @PostMapping
    public Candidature createCandidature(@RequestBody Candidature candidature) {
        return candidatureRepository.save(candidature);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidature> updateCandidature(@PathVariable Long id, @RequestBody Candidature candidatureDetails) {
        return candidatureRepository.findById(id)
                .map(candidature -> {
                    candidature.setNom(candidatureDetails.getNom());
                    candidature.setEmail(candidatureDetails.getEmail());
                    candidature.setCv(candidatureDetails.getCv());
                    candidature.setDateCandidature(candidatureDetails.getDateCandidature());
                    candidature.setOffre(candidatureDetails.getOffre());
                    return ResponseEntity.ok(candidatureRepository.save(candidature));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCandidature(@PathVariable Long id) {
        return candidatureRepository.findById(id)
                .map(candidature -> {
                    candidatureRepository.delete(candidature);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 