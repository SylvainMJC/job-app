package fr.epsi.b3devc1.sylvainmjc.backend.controller;

import fr.epsi.b3devc1.sylvainmjc.backend.entity.Offre;
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
class OffreControllerTest {

    @Mock
    private OffreRepository offreRepository;

    @InjectMocks
    private OffreController offreController;

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
    }

    @Test
    void getAllOffres_shouldReturnAllOffres() {
        // Arrange
        List<Offre> offres = Arrays.asList(offre, new Offre());
        when(offreRepository.findAll()).thenReturn(offres);

        // Act
        List<Offre> result = offreController.getAllOffres();

        // Assert
        assertEquals(2, result.size());
        verify(offreRepository, times(1)).findAll();
    }

    @Test
    void getOffreById_shouldReturnOffre_whenOffreExists() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.of(offre));

        // Act
        ResponseEntity<Offre> response = offreController.getOffreById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offre, response.getBody());
        verify(offreRepository, times(1)).findById(1L);
    }

    @Test
    void getOffreById_shouldReturnNotFound_whenOffreDoesNotExist() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Offre> response = offreController.getOffreById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(offreRepository, times(1)).findById(1L);
    }

    @Test
    void createOffre_shouldCreateOffre() {
        // Arrange
        when(offreRepository.save(any(Offre.class))).thenReturn(offre);

        // Act
        Offre result = offreController.createOffre(offre);

        // Assert
        assertEquals(offre, result);
        verify(offreRepository, times(1)).save(offre);
    }

    @Test
    void updateOffre_shouldUpdateOffre_whenOffreExists() {
        // Arrange
        Offre updatedOffre = new Offre();
        updatedOffre.setTitre("Titre mis à jour");
        updatedOffre.setEntreprise("Entreprise mise à jour");
        updatedOffre.setLocalisation("Localisation mise à jour");
        updatedOffre.setDescription("Description mise à jour");
        updatedOffre.setDateCreation(LocalDateTime.now());

        when(offreRepository.findById(1L)).thenReturn(Optional.of(offre));
        when(offreRepository.save(any(Offre.class))).thenReturn(offre);

        // Act
        ResponseEntity<Offre> response = offreController.updateOffre(1L, updatedOffre);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(offreRepository, times(1)).findById(1L);
        verify(offreRepository, times(1)).save(offre);
    }

    @Test
    void updateOffre_shouldReturnNotFound_whenOffreDoesNotExist() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Offre> response = offreController.updateOffre(1L, new Offre());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(offreRepository, times(1)).findById(1L);
        verify(offreRepository, never()).save(any(Offre.class));
    }

    @Test
    void deleteOffre_shouldDeleteOffre_whenOffreExists() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.of(offre));
        doNothing().when(offreRepository).delete(offre);

        // Act
        ResponseEntity<?> response = offreController.deleteOffre(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(offreRepository, times(1)).findById(1L);
        verify(offreRepository, times(1)).delete(offre);
    }

    @Test
    void deleteOffre_shouldReturnNotFound_whenOffreDoesNotExist() {
        // Arrange
        when(offreRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = offreController.deleteOffre(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(offreRepository, times(1)).findById(1L);
        verify(offreRepository, never()).delete(any(Offre.class));
    }
} 