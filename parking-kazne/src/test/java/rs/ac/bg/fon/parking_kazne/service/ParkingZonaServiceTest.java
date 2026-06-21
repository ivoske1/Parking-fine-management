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
import rs.ac.bg.fon.parking_kazne.dto.ParkingZonaRequest;
import rs.ac.bg.fon.parking_kazne.dto.ParkingZonaResponse;
import rs.ac.bg.fon.parking_kazne.entity.ParkingZona;
import rs.ac.bg.fon.parking_kazne.enums.Zona;
import rs.ac.bg.fon.parking_kazne.repository.ParkinZonaRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ParkingZonaServiceTest {

    @Mock
    private ParkinZonaRepository parkinZonaRepository;

    @InjectMocks
    private ParkingZonaService parkingZonaService;

    private ParkingZona mockParkingZona;

    @BeforeEach
    void setUp() {
        mockParkingZona = new ParkingZona();
        mockParkingZona.setId(1L);
        mockParkingZona.setNaziv("Crvena Zona A");
        mockParkingZona.setCenaPoSatu(100.0);
        mockParkingZona.setZona(Zona.CRVENA);
    }

    @Test
    @DisplayName("Should successfully create a new parking zone configuration")
    void create_Success() {
        when(parkinZonaRepository.save(any(ParkingZona.class))).thenReturn(mockParkingZona);

        ParkingZonaRequest request = new ParkingZonaRequest("Crvena Zona A", 100.0, Zona.CRVENA);
        ParkingZonaResponse response = parkingZonaService.create(request);

        assertNotNull(response);
        verify(parkinZonaRepository).save(any(ParkingZona.class));
    }

    @Test
    @DisplayName("Should return all registered parking zones")
    void findAll_Success() {
        when(parkinZonaRepository.findAll()).thenReturn(List.of(mockParkingZona));

        List<ParkingZonaResponse> responseList = parkingZonaService.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(parkinZonaRepository).findAll();
    }

    @Test
    @DisplayName("Should return specific parking zone when findById matches existing record")
    void findById_Success() {
        when(parkinZonaRepository.findById(1L)).thenReturn(Optional.of(mockParkingZona));

        ParkingZonaResponse response = parkingZonaService.findById(1L);

        assertNotNull(response);
        verify(parkinZonaRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when internal lookup targets non existing zone")
    void findByInternalId_NotFound_ThrowsException() {
        when(parkinZonaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                parkingZonaService.findByInternalId(99L)
        );

        assertEquals("ParkingZona not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully execute repository delete when record is found")
    void deleteById_Success() {
        when(parkinZonaRepository.findById(1L)).thenReturn(Optional.of(mockParkingZona));
        doNothing().when(parkinZonaRepository).deleteById(1L);

        assertDoesNotThrow(() -> parkingZonaService.deleteById(1L));

        verify(parkinZonaRepository).deleteById(1L);
    }
}