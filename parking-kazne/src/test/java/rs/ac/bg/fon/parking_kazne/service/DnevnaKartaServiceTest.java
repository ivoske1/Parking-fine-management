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
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaRequest;
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaResponse;
import rs.ac.bg.fon.parking_kazne.entity.DnevnaKarta;
import rs.ac.bg.fon.parking_kazne.entity.ParkingZona;
import rs.ac.bg.fon.parking_kazne.entity.Vozilo;
import rs.ac.bg.fon.parking_kazne.repository.DnevnaKartaRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DnevnaKartaServiceTest {

    @Mock
    private DnevnaKartaRepository dnevnaKartaRepository;

    @Mock
    private VoziloService voziloService;

    @Mock
    private ParkingZonaService parkingZonaService;

    @InjectMocks
    private DnevnaKartaService dnevnaKartaService;

    private Vozilo mockVozilo;
    private ParkingZona mockParkingZona;
    private DnevnaKarta mockDnevnaKarta;

    @BeforeEach
    void setUp() {
        mockVozilo = new Vozilo();
        mockVozilo.setId(1L);
        mockVozilo.setRegistracija("BG-123-XX");

        mockParkingZona = new ParkingZona();
        mockParkingZona.setId(1L);
        mockParkingZona.setNaziv("Crvena Zona");
        mockParkingZona.setCenaPoSatu(100.0);

        mockDnevnaKarta = new DnevnaKarta();
        mockDnevnaKarta.setId(10L);
        mockDnevnaKarta.setVozilo(mockVozilo);
        mockDnevnaKarta.setParkingZona(mockParkingZona);
        mockDnevnaKarta.setBrojSati(5);
        mockDnevnaKarta.setVremeKupovine(LocalDateTime.now());
        mockDnevnaKarta.setVaziDo(LocalDateTime.now().plusHours(5));
        mockDnevnaKarta.setCena(500.0);
    }

    @Test
    @DisplayName("Should successfully create daily ticket when vehicle has no active tickets")
    void create_Success() {
        when(dnevnaKartaRepository.findAll()).thenReturn(Collections.emptyList());
        when(voziloService.findByRegistracija("BG-123-XX")).thenReturn(mockVozilo);
        when(parkingZonaService.findByInternalId(1L)).thenReturn(mockParkingZona);
        when(dnevnaKartaRepository.save(any(DnevnaKarta.class))).thenReturn(mockDnevnaKarta);

        DnevnaKartaRequest request = new DnevnaKartaRequest(5, "BG-123-XX", 1L);
        DnevnaKartaResponse response = dnevnaKartaService.create(request);

        assertNotNull(response);
        assertEquals(500.0, response.cena());
        verify(dnevnaKartaRepository).save(any(DnevnaKarta.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when vehicle already holds an active ticket")
    void create_VehicleAlreadyHasTicket_ThrowsException() {
        DnevnaKarta aktivnaKarta = new DnevnaKarta();
        aktivnaKarta.setVozilo(mockVozilo);
        aktivnaKarta.setVaziDo(LocalDateTime.now().plusHours(2));
        aktivnaKarta.setParkingZona(mockParkingZona);

        when(dnevnaKartaRepository.findAll()).thenReturn(List.of(aktivnaKarta));

        DnevnaKartaRequest request = new DnevnaKartaRequest(5, "BG-123-XX", 1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                dnevnaKartaService.create(request)
        );

        assertEquals("Vehicle already has a ticket", exception.getMessage());
        verify(dnevnaKartaRepository, never()).save(any(DnevnaKarta.class));
    }

    @Test
    @DisplayName("Should return specific ticket when findById matches existing record")
    void findById_Success() {
        when(dnevnaKartaRepository.findById(10L)).thenReturn(Optional.of(mockDnevnaKarta));

        DnevnaKartaResponse response = dnevnaKartaService.findById(10L);

        assertNotNull(response);
        assertEquals(10L, response.id());
    }

    @Test
    @DisplayName("Should throw RuntimeException when findById targets non existing record")
    void findById_NotFound_ThrowsException() {
        when(dnevnaKartaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                dnevnaKartaService.findById(99L)
        );

        assertEquals("Daily ticket not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully execute repository delete when record exists")
    void deleteById_Success() {
        when(dnevnaKartaRepository.findById(10L)).thenReturn(Optional.of(mockDnevnaKarta));
        doNothing().when(dnevnaKartaRepository).deleteById(10L);

        assertDoesNotThrow(() -> dnevnaKartaService.deleteById(10L));

        verify(dnevnaKartaRepository).deleteById(10L);
    }
}