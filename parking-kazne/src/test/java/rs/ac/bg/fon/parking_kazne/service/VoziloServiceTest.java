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
import rs.ac.bg.fon.parking_kazne.dto.VoziloRequest;
import rs.ac.bg.fon.parking_kazne.dto.VoziloResponse;
import rs.ac.bg.fon.parking_kazne.entity.Vozac;
import rs.ac.bg.fon.parking_kazne.entity.Vozilo;
import rs.ac.bg.fon.parking_kazne.repository.VoziloRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class VoziloServiceTest {

    @Mock
    private VoziloRepository voziloRepository;

    @Mock
    private VozacService vozacService;

    @InjectMocks
    private VoziloService voziloService;

    private Vozilo mockVozilo;
    private Vozac mockVozac;

    @BeforeEach
    void setUp() {
        mockVozac = new Vozac();
        mockVozac.setId(10L);
        mockVozac.setIme("Nikola");
        mockVozac.setPrezime("Nikolić");

        mockVozilo = new Vozilo();
        mockVozilo.setId(1L);
        mockVozilo.setRegistracija("BG-123-XX");
        mockVozilo.setMarka("Audi");
        mockVozilo.setModel("A4");
        mockVozilo.setVozac(mockVozac);
    }

    @Test
    @DisplayName("Should successfully create a new vehicle and link it to an existing driver")
    void create_Success() {
        when(vozacService.findByInternalId(10L)).thenReturn(mockVozac);
        when(voziloRepository.save(any(Vozilo.class))).thenReturn(mockVozilo);

        VoziloRequest request = new VoziloRequest("BG-123-XX", "Audi", "A4", 10L);
        VoziloResponse response = voziloService.create(request);

        assertNotNull(response);
        verify(vozacService).findByInternalId(10L);
        verify(voziloRepository).save(any(Vozilo.class));
    }

    @Test
    @DisplayName("Should return all registered vehicles")
    void findAll_Success() {
        when(voziloRepository.findAll()).thenReturn(List.of(mockVozilo));

        List<VoziloResponse> responseList = voziloService.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(voziloRepository).findAll();
    }

    @Test
    @DisplayName("Should return specific vehicle when findById matches record")
    void findById_Success() {
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(mockVozilo));

        VoziloResponse response = voziloService.findById(1L);

        assertNotNull(response);
        verify(voziloRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when internal lookup targets non existing vehicle")
    void findByIdInternal_NotFound_ThrowsException() {
        when(voziloRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                voziloService.findByIdInternal(99L)
        );

        assertEquals("Vehicle not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully delete vehicle from system when record is found")
    void deleteById_Success() {
        when(voziloRepository.findById(1L)).thenReturn(Optional.of(mockVozilo));
        doNothing().when(voziloRepository).deleteById(1L);

        assertDoesNotThrow(() -> voziloService.deleteById(1L));

        verify(voziloRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should successfully find vehicle by license plate string")
    void findByRegistracija_Success() {
        when(voziloRepository.findAll()).thenReturn(List.of(mockVozilo));

        Vozilo pronadjeno = voziloService.findByRegistracija("bg-123-xx");

        assertNotNull(pronadjeno);
        assertEquals("BG-123-XX", pronadjeno.getRegistracija());
    }

    @Test
    @DisplayName("Should throw RuntimeException when no vehicle matches target registration")
    void findByRegistracija_NotFound_ThrowsException() {
        when(voziloRepository.findAll()).thenReturn(List.of(mockVozilo));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                voziloService.findByRegistracija("NS-999-AA")
        );

        assertEquals("Not found vehicle", exception.getMessage());
    }
}