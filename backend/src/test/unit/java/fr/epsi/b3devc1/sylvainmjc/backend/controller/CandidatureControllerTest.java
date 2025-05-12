package fr.epsi.b3devc1.sylvainmjc.backend.controller;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Candidature;
import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.CandidatureRepository;
import fr.epsi.b3devc1.sylvainmjc.backend.repository.OffreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@ActiveProfiles("test")
class CandidatureControllerTest {

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private OffreRepository offreRepository;

    @InjectMocks
    private CandidatureController candidatureController;

    private Candidature candidature;
    private Offre offre;

    @BeforeEach
    void setUp() {
        offre = new Offre();
        offre.setId(1L);
        offre.setTitre("Développeur Java");
        offre.setEntreprise("EPSI");
        offre.setLocalisation("Lyon");
        offre.setDescription("Description du poste");
        offre.setDateCreation(LocalDateTime.now());

        candidature = new Candidature();
        candidature.setId(1L);
        candidature.setNom("Dupont");
        candidature.setPrenom("Jean");
        candidature.setEmail("jean.dupont@example.com");
        candidature.setTelephone("0123456789");
        candidature.setMessage("Je suis intéressé par ce poste");
        candidature.setDateCandidat(LocalDateTime.now());
        candidature.setOffre(offre);
    }

    @Test
    void getAllCandidatures_shouldReturnAllCandidatures() {
        // Arrange
        List<Candidature> candidatures = Arrays.asList(candidature, new Candidature());
        when(candidatureRepository.findAll()).thenReturn(candidatures);

        // Act
        List<Candidature> result = candidatureController.getAllCandidatures();

        // Assert
        assertEquals(2, result.size());
        verify(candidatureRepository, times(1)).findAll();
    }

    @Test
    void getCandidatureById_shouldReturnCandidature_whenCandidatureExists() {
        // Arrange
        when(candidatureRepository.findById(1L)).thenReturn(Optional.of(candidature));

        // Act
        ResponseEntity<Candidature> response = candidatureController.getCandidatureById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidature, response.getBody());
        verify(candidatureRepository, times(1)).findById(1L);
    }

    @Test
    void getCandidatureById_shouldReturnNotFound_whenCandidatureDoesNotExist() {
        // Arrange
        when(candidatureRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Candidature> response = candidatureController.getCandidatureById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(candidatureRepository, times(1)).findById(1L);
    }

    @Test
    void getCandidaturesByOffre_shouldReturnCandidatures_whenOffreExists() {
        // Arrange
        List<Candidature> candidatures = Arrays.asList(candidature);
        when(offreRepository.findById(1L)).thenReturn(Optional.of(offre));
        when(candidatureRepository.findByOffre(offre)).thenReturn(candidatures);

        // Act
        ResponseEntity<List<Candidature>> response = candidatureController.getCandidaturesByOffre(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(offreRepository, times(1)).findById(1L);
        verify(candidatureRepository, times(1)).findByOffre(offre);
    }

    @Test
    void getCandidaturesByOffre_shouldReturnNotFound_whenOffreDoesNotExist() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<List<Candidature>> response = candidatureController.getCandidaturesByOffre(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(offreRepository, times(1)).findById(1L);
        verify(candidatureRepository, never()).findByOffre(any(Offre.class));
    }

    @Test
    void createCandidature_shouldCreateCandidature() {
        // Arrange
        when(candidatureRepository.save(any(Candidature.class))).thenReturn(candidature);

        // Act
        Candidature result = candidatureController.createCandidature(candidature);

        // Assert
        assertEquals(candidature, result);
        verify(candidatureRepository, times(1)).save(candidature);
    }

    @Test
    void createCandidatureForOffre_shouldCreateCandidature_whenOffreExists() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.of(offre));
        when(candidatureRepository.save(any(Candidature.class))).thenReturn(candidature);

        // Act
        ResponseEntity<Candidature> response = candidatureController.createCandidature(1L, candidature);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(candidature, response.getBody());
        verify(offreRepository, times(1)).findById(1L);
        verify(candidatureRepository, times(1)).save(candidature);
    }

    @Test
    void createCandidatureForOffre_shouldReturnNotFound_whenOffreDoesNotExist() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Candidature> response = candidatureController.createCandidature(1L, candidature);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(offreRepository, times(1)).findById(1L);
        verify(candidatureRepository, never()).save(any(Candidature.class));
    }

    @Test
    void updateCandidature_shouldUpdateCandidature_whenCandidatureExists() {
        // Arrange
        Candidature updatedCandidature = new Candidature();
        updatedCandidature.setNom("Nom mis à jour");
        updatedCandidature.setPrenom("Prénom mis à jour");
        updatedCandidature.setEmail("email.mis.a.jour@example.com");
        updatedCandidature.setTelephone("9876543210");
        updatedCandidature.setMessage("Message mis à jour");
        updatedCandidature.setDateCandidat(LocalDateTime.now());

        when(candidatureRepository.findById(1L)).thenReturn(Optional.of(candidature));
        when(candidatureRepository.save(any(Candidature.class))).thenReturn(candidature);

        // Act
        ResponseEntity<Candidature> response = candidatureController.updateCandidature(1L, updatedCandidature);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(candidatureRepository, times(1)).findById(1L);
        verify(candidatureRepository, times(1)).save(candidature);
    }

    @Test
    void updateCandidature_shouldReturnNotFound_whenCandidatureDoesNotExist() {
        // Arrange
        when(candidatureRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Candidature> response = candidatureController.updateCandidature(1L, new Candidature());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(candidatureRepository, times(1)).findById(1L);
        verify(candidatureRepository, never()).save(any(Candidature.class));
    }

    @Test
    void deleteCandidature_shouldDeleteCandidature_whenCandidatureExists() {
        // Arrange
        when(candidatureRepository.findById(1L)).thenReturn(Optional.of(candidature));
        doNothing().when(candidatureRepository).delete(candidature);

        // Act
        ResponseEntity<?> response = candidatureController.deleteCandidature(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(candidatureRepository, times(1)).findById(1L);
        verify(candidatureRepository, times(1)).delete(candidature);
    }

    @Test
    void deleteCandidature_shouldReturnNotFound_whenCandidatureDoesNotExist() {
        // Arrange
        when(candidatureRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = candidatureController.deleteCandidature(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(candidatureRepository, times(1)).findById(1L);
        verify(candidatureRepository, never()).delete(any(Candidature.class));
    }
} 