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
import rs.ac.bg.fon.parking_kazne.dto.KontrolorRequest;
import rs.ac.bg.fon.parking_kazne.dto.KontrolorResponse;
import rs.ac.bg.fon.parking_kazne.entity.Kontrolor;
import rs.ac.bg.fon.parking_kazne.repository.KontrolorRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class KontrolorServiceTest {

    @Mock
    private KontrolorRepository kontrolorRepository;

    @InjectMocks
    private KontrolorService kontrolorService;

    private Kontrolor mockKontrolor;

    @BeforeEach
    void setUp() {
        mockKontrolor = new Kontrolor();
        mockKontrolor.setId(1L);
        mockKontrolor.setIme("Marko");
        mockKontrolor.setPrezime("Marković");
        mockKontrolor.setBrojLegitimacije("LEG-12345");
    }

    @Test
    @DisplayName("Should successfully create a new controller officer")
    void create_Success() {
        when(kontrolorRepository.save(any(Kontrolor.class))).thenReturn(mockKontrolor);

        KontrolorRequest request = new KontrolorRequest("Marko", "Marković", "LEG-12345");
        KontrolorResponse response = kontrolorService.create(request);

        assertNotNull(response);
        verify(kontrolorRepository).save(any(Kontrolor.class));
    }

    @Test
    @DisplayName("Should return all registered controllers logged in system")
    void findAll_Success() {
        when(kontrolorRepository.findAll()).thenReturn(List.of(mockKontrolor));

        List<KontrolorResponse> responseList = kontrolorService.findAll();

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        verify(kontrolorRepository).findAll();
    }

    @Test
    @DisplayName("Should return specific controller when findById matches existing record")
    void findById_Success() {
        when(kontrolorRepository.findById(1L)).thenReturn(Optional.of(mockKontrolor));

        KontrolorResponse response = kontrolorService.findById(1L);

        assertNotNull(response);
        verify(kontrolorRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw RuntimeException when internal lookup targets non existing record")
    void findByIdInternal_NotFound_ThrowsException() {
        when(kontrolorRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                kontrolorService.findByIdInternal(99L)
        );

        assertEquals("Kontrolor not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully execute repository delete when record is found")
    void deleteById_Success() {
        when(kontrolorRepository.findById(1L)).thenReturn(Optional.of(mockKontrolor));
        doNothing().when(kontrolorRepository).deleteById(1L);

        assertDoesNotThrow(() -> kontrolorService.deleteById(1L));

        verify(kontrolorRepository).deleteById(1L);
    }
}