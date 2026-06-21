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
import rs.ac.bg.fon.parking_kazne.dto.KaznaRequest;
import rs.ac.bg.fon.parking_kazne.dto.KaznaResponse;
import rs.ac.bg.fon.parking_kazne.entity.*;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;
import rs.ac.bg.fon.parking_kazne.enums.Zona;
import rs.ac.bg.fon.parking_kazne.repository.KaznaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class KaznaServiceTest {

    @Mock
    private KaznaRepository kaznaRepository;
    @Mock
    private VoziloService voziloService;
    @Mock
    private KontrolorService kontrolorService;
    @Mock
    private LokacijaService lokacijaService;
    @Mock
    private ParkingZonaService parkingZonaService;

    @InjectMocks
    private KaznaService kaznaService;

    private Vozilo mockVozilo;
    private Kontrolor mockKontrolor;
    private Lokacija mockLokacija;
    private ParkingZona mockParkingZona;
    private Kazna mockKazna;

    @BeforeEach
    void setUp() {
        mockVozilo = new Vozilo();
        mockVozilo.setId(1L);
        mockVozilo.setRegistracija("BG-123-XX");

        mockKontrolor = new Kontrolor();
        mockKontrolor.setId(2L);

        mockLokacija = new Lokacija();
        mockLokacija.setId(3L);

        mockParkingZona = new ParkingZona();
        mockParkingZona.setId(4L);
        mockParkingZona.setZona(Zona.CRVENA);

        mockKazna = new Kazna();
        mockKazna.setId(100L);
        mockKazna.setVremeIzdavanja(LocalDateTime.now());
        mockKazna.setStatusKazne(StatusKazne.NEPLACENA);
        mockKazna.setIznos(5000.0); // CRVENA zona nosi 5000 dinara kazne
        mockKazna.setVozilo(mockVozilo);
        mockKazna.setKontrolor(mockKontrolor);
        mockKazna.setLokacija(mockLokacija);
        mockKazna.setParkingZona(mockParkingZona);
    }

    @Test
    @DisplayName("Should successfully issue a ticket and automatically calculate fine amount based on zone")
    void create_Success() {
        when(voziloService.findByRegistracija(anyString())).thenReturn(mockVozilo);
        when(kontrolorService.findByIdInternal(anyLong())).thenReturn(mockKontrolor);
        when(lokacijaService.findByIdInternal(anyLong())).thenReturn(mockLokacija);
        when(parkingZonaService.findByInternalId(anyLong())).thenReturn(mockParkingZona);

        when(kaznaRepository.save(any(Kazna.class))).thenReturn(mockKazna);

        KaznaRequest request = new KaznaRequest("BG-123-XX", 2L, 3L, 4L);
        KaznaResponse response = kaznaService.create(request);

        assertNotNull(response);
        assertEquals(5000.0, response.iznos());
        assertEquals(StatusKazne.NEPLACENA, response.status());
        verify(kaznaRepository).save(any(Kazna.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when target ticket for lookup does not exist")
    void findByIdInternal_NotFound_ThrowsException() {
        when(kaznaRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                kaznaService.findByIdInternal(999L)
        );

        assertEquals("Ticket does not exists", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully execute repository delete when record is found")
    void deleteById_Success() {
        when(kaznaRepository.findById(100L)).thenReturn(Optional.of(mockKazna));
        doNothing().when(kaznaRepository).deleteById(100L);

        assertDoesNotThrow(() -> kaznaService.deleteById(100L));

        verify(kaznaRepository).deleteById(100L);
    }
}