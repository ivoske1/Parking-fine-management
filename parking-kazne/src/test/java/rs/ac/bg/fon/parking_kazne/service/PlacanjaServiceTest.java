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
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeRequest;
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeResponse;
import rs.ac.bg.fon.parking_kazne.entity.Kazna;
import rs.ac.bg.fon.parking_kazne.entity.Placanje;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;
import rs.ac.bg.fon.parking_kazne.repository.PlacanjeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PlacanjaServiceTest {

    @Mock
    private KaznaService kaznaService;

    @Mock
    private PlacanjeRepository placanjeRepository;

    @InjectMocks
    private PlacanjaService placanjaService;

    private Kazna mockKazna;
    private Placanje mockPlacanje;

    @BeforeEach
    void setUp() {
        mockKazna = new Kazna();
        mockKazna.setId(50L);
        mockKazna.setIznos(5000.0);
        mockKazna.setStatusKazne(StatusKazne.NEPLACENA);

        mockPlacanje = new Placanje();
        mockPlacanje.setId(100L);
        mockPlacanje.setKazna(mockKazna);
        mockPlacanje.setIznos(5000.0);
        mockPlacanje.setDatum(LocalDate.now());
    }

    @Test
    @DisplayName("Should successfully process payment when ticket is unpaid and amount is sufficient")
    void create_Success() {
        when(kaznaService.findByIdInternal(50L)).thenReturn(mockKazna);
        when(kaznaService.updateStatus(50L, StatusKazne.PLACENA)).thenReturn(null);
        when(placanjeRepository.save(any(Placanje.class))).thenReturn(mockPlacanje);

        PlacanjeRequest request = new PlacanjeRequest(50L, 5000.0, null);
        PlacanjeResponse response = placanjaService.create(request);

        assertNotNull(response);
        verify(kaznaService).updateStatus(50L, StatusKazne.PLACENA);
        verify(placanjeRepository).save(any(Placanje.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when processing payment for an already paid ticket")
    void create_TicketAlreadyPaid_ThrowsException() {
        mockKazna.setStatusKazne(StatusKazne.PLACENA);
        when(kaznaService.findByIdInternal(50L)).thenReturn(mockKazna);

        PlacanjeRequest request = new PlacanjeRequest(50L, 5000.0, null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                placanjaService.create(request)
        );

        assertEquals("Ticket is already payed", exception.getMessage());
        verify(kaznaService, never()).updateStatus(anyLong(), any());
        verify(placanjeRepository, never()).save(any(Placanje.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when paid amount is less than ticket fine amount")
    void create_InvalidPaymentAmount_ThrowsException() {
        when(kaznaService.findByIdInternal(50L)).thenReturn(mockKazna); // Kazna je 5000.0

        PlacanjeRequest request = new PlacanjeRequest(50L, 3000.0, null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                placanjaService.create(request)
        );

        assertEquals("Invalid payment", exception.getMessage());
        verify(kaznaService, never()).updateStatus(anyLong(), any());
        verify(placanjeRepository, never()).save(any(Placanje.class));
    }

    @Test
    @DisplayName("Should return all logged payments")
    void findAll_Success() {
        when(placanjeRepository.findAll()).thenReturn(List.of(mockPlacanje));

        List<PlacanjeResponse> responseList = placanjaService.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(placanjeRepository).findAll();
    }

    @Test
    @DisplayName("Should throw RuntimeException when internal lookup targets non existing transaction")
    void findByIdInternal_NotFound_ThrowsException() {
        when(placanjeRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                placanjaService.findById(999L) // findById javna metoda interno poziva privatnu
        );

        assertEquals("No payment in system", exception.getMessage());
    }
}