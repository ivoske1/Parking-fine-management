package rs.ac.bg.fon.parking_kazne.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaRequest;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaResponse;
import rs.ac.bg.fon.parking_kazne.entity.Lokacija;
import rs.ac.bg.fon.parking_kazne.repository.LokacijaRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LokacijaServiceTest {

    @Mock
    private LokacijaRepository lokacijaRepository;

    @InjectMocks
    private LokacijaService lokacijaService;

    private Lokacija mockLokacija;

    @BeforeEach
    void setUp() {
        mockLokacija = new Lokacija();
        mockLokacija.setId(1L);
        mockLokacija.setUlica("Jove Ilića 154");
        mockLokacija.setGrad("Beograd");
    }

    @Test
    @DisplayName("Should successfully create and store a new location record")
    void create_Success() {
        when(lokacijaRepository.save(any(Lokacija.class))).thenReturn(mockLokacija);

        LokacijaRequest request = new LokacijaRequest("Jove Ilića 154", "Beograd");
        LokacijaResponse response = lokacijaService.create(request);

        assertNotNull(response);
        verify(lokacijaRepository).save(any(Lokacija.class));
    }

    @Test
    @DisplayName("Should return all configured locations logged in system")
    void findAll_Success() {
        when(lokacijaRepository.findAll()).thenReturn(List.of(mockLokacija));

        List<LokacijaResponse> responseList = lokacijaService.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(lokacijaRepository).findAll();
    }

    @Test
    @DisplayName("Should return specific location profile when findById matches record")
    void findById_Success() {
        when(lokacijaRepository.findById(1L)).thenReturn(Optional.of(mockLokacija));

        LokacijaResponse response = lokacijaService.findById(1L);

        assertNotNull(response);
        verify(lokacijaRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when findByIdInternal targets non existing location")
    void findByIdInternal_NotFound_ThrowsException() {
        when(lokacijaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                lokacijaService.findByIdInternal(99L)
        );

        assertEquals("Location not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully execute repository delete when record is found")
    void deleteById_Success() {
        when(lokacijaRepository.findById(1L)).thenReturn(Optional.of(mockLokacija));
        doNothing().when(lokacijaRepository).deleteById(1L);

        assertDoesNotThrow(() -> lokacijaService.deleteById(1L));

        verify(lokacijaRepository).deleteById(1L);
    }
}