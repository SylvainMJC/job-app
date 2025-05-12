package fr.epsi.b3devc1.sylvainmjc.backend.controller;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.OffreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffreController {

    @Autowired
    private OffreRepository offreRepository;

    @GetMapping
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    @PostMapping
    public Offre createOffre(@RequestBody Offre offre) {
        return offreRepository.save(offre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Offre> updateOffre(@PathVariable Long id, @RequestBody Offre offreDetails) {
        return offreRepository.findById(id)
                .map(offre -> {
                    offre.setTitre(offreDetails.getTitre());
                    offre.setDescription(offreDetails.getDescription());
                    offre.setDateCreation(offreDetails.getDateCreation());
                    return ResponseEntity.ok(offreRepository.save(offre));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOffre(@PathVariable Long id) {
        return offreRepository.findById(id)
                .map(offre -> {
                    offreRepository.delete(offre);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 