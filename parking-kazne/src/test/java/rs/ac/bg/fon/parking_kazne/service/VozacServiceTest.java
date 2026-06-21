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
import rs.ac.bg.fon.parking_kazne.dto.VozacRequest;
import rs.ac.bg.fon.parking_kazne.dto.VozacResponse;
import rs.ac.bg.fon.parking_kazne.entity.Vozac;
import rs.ac.bg.fon.parking_kazne.repository.VozacRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class VozacServiceTest {

    @Mock
    private VozacRepository vozacRepository;

    @InjectMocks
    private VozacService vozacService;

    private Vozac mockVozac;

    @BeforeEach
    void setUp() {
        mockVozac = new Vozac();
        mockVozac.setId(1L);
        mockVozac.setIme("Nikola");
        mockVozac.setPrezime("Nikolić");
        mockVozac.setBrojVozacke("VUZ-98765");
        mockVozac.setTelefon("+381641234567");
    }

    @Test
    @DisplayName("Should successfully register a new driver")
    void create_Success() {
        when(vozacRepository.save(any(Vozac.class))).thenReturn(mockVozac);

        VozacRequest request = new VozacRequest("Nikola", "Nikolić", "VUZ-98765", "+381641234567");
        VozacResponse response = vozacService.create(request);

        assertNotNull(response);
        verify(vozacRepository).save(any(Vozac.class));
    }

    @Test
    @DisplayName("Should return all registered drivers")
    void findAll_Success() {
        when(vozacRepository.findAll()).thenReturn(List.of(mockVozac));

        List<VozacResponse> responseList = vozacService.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(vozacRepository).findAll();
    }

    @Test
    @DisplayName("Should return specific driver profile when findById matches record")
    void findById_Success() {
        when(vozacRepository.findById(1L)).thenReturn(Optional.of(mockVozac));

        VozacResponse response = vozacService.findById(1L);

        assertNotNull(response);
        verify(vozacRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when internal lookup targets non existing driver")
    void findByInternalId_NotFound_ThrowsException() {
        when(vozacRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                vozacService.findByInternalId(99L)
        );

        assertEquals("Driver not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully execute repository delete when driver is found")
    void deleteById_Success() {
        when(vozacRepository.findById(1L)).thenReturn(Optional.of(mockVozac));
        doNothing().when(vozacRepository).deleteById(1L);

        assertDoesNotThrow(() -> vozacService.deleteById(1L));

        verify(vozacRepository).deleteById(1L);
    }
}