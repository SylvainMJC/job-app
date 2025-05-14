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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@ActiveProfiles("test")
public class OffreControllerTest {

    @Mock
    private OffreRepository offreRepository;

    @InjectMocks
    private OffreController offreController;

    private Offre testOffre;

    @BeforeEach
    void setUp() {
        testOffre = new Offre();
        testOffre.setId(1L);
        testOffre.setTitre("Développeur Java");
        testOffre.setEntreprise("EPSI");
        testOffre.setLocalisation("Lyon");
        testOffre.setDescription("Description du poste");
    }

    @Test
    void getAllOffres_ShouldReturnListOfOffres() {
        List<Offre> offres = Arrays.asList(testOffre, new Offre());
        when(offreRepository.findAll()).thenReturn(offres);

        List<Offre> result = offreController.getAllOffres();

        assertEquals(2, result.size());
        verify(offreRepository, times(1)).findAll();
    }

    @Test
    void getOffreById_WhenOffreExists_ShouldReturnOffre() {
        when(offreRepository.findById(1L)).thenReturn(Optional.of(testOffre));

        ResponseEntity<Offre> response = offreController.getOffreById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Développeur Java", response.getBody().getTitre());
        verify(offreRepository, times(1)).findById(1L);
    }
} 