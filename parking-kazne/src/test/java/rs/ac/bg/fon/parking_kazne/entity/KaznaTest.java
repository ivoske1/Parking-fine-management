package rs.ac.bg.fon.parking_kazne.entity;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;

import java.time.LocalDateTime;
import java.util.Set;

class KaznaTest {
    private Validator validator;
    private Vozilo mockVozilo;
    private Kontrolor mockKontrolor;
    private Lokacija mockLokacija;
    private ParkingZona mockParkingZona;
    private Placanje mockPlacanje;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockVozilo = Mockito.mock(Vozilo.class);
        mockKontrolor = Mockito.mock(Kontrolor.class);
        mockLokacija = Mockito.mock(Lokacija.class);
        mockParkingZona = Mockito.mock(ParkingZona.class);
        mockPlacanje = Mockito.mock(Placanje.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should pass validation with valid Kazna object")
    void validate_ValidKazna_NoViolations() {
        Kazna kazna = new Kazna(
                null,
                LocalDateTime.of(2025, 6, 1, 10, 30),
                5000.0,
                StatusKazne.NEPLACENA,
                mockVozilo,
                mockKontrolor,
                mockLokacija,
                mockParkingZona,
                null
        );

        Set<ConstraintViolation<Kazna>> violations = validator.validate(kazna);

        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(StatusKazne.class)
    @DisplayName("Should pass validation for all StatusKazne enum values")
    void validate_AllStatusKazneValues_NoViolations(StatusKazne status) {
        Kazna kazna = new Kazna(
                null,
                LocalDateTime.now(),
                3000.0,
                status,
                mockVozilo,
                mockKontrolor,
                mockLokacija,
                mockParkingZona,
                null
        );

        Set<ConstraintViolation<Kazna>> violations = validator.validate(kazna);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        LocalDateTime vremeIzdavanja = LocalDateTime.of(2025, 6, 1, 10, 30);

        Kazna kazna = new Kazna();
        kazna.setId(1L);
        kazna.setVremeIzdavanja(vremeIzdavanja);
        kazna.setIznos(5000.0);
        kazna.setStatusKazne(StatusKazne.NEPLACENA);
        kazna.setVozilo(mockVozilo);
        kazna.setKontrolor(mockKontrolor);
        kazna.setLokacija(mockLokacija);
        kazna.setParkingZona(mockParkingZona);
        kazna.setPlacanje(mockPlacanje);

        assertEquals(1L, kazna.getId());
        assertEquals(vremeIzdavanja, kazna.getVremeIzdavanja());
        assertEquals(5000.0, kazna.getIznos());
        assertEquals(StatusKazne.NEPLACENA, kazna.getStatusKazne());
        assertEquals(mockVozilo, kazna.getVozilo());
        assertEquals(mockKontrolor, kazna.getKontrolor());
        assertEquals(mockLokacija, kazna.getLokacija());
        assertEquals(mockParkingZona, kazna.getParkingZona());
        assertEquals(mockPlacanje, kazna.getPlacanje());
    }

    @Test
    @DisplayName("Should successfully create Kazna with all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime vremeIzdavanja = LocalDateTime.of(2025, 5, 20, 14, 0);

        Kazna kazna = new Kazna(
                99L,
                vremeIzdavanja,
                3000.0,
                StatusKazne.PLACENA,
                mockVozilo,
                mockKontrolor,
                mockLokacija,
                mockParkingZona,
                mockPlacanje
        );

        assertNotNull(kazna);
        assertEquals(99L, kazna.getId());
        assertEquals(vremeIzdavanja, kazna.getVremeIzdavanja());
        assertEquals(3000.0, kazna.getIznos());
        assertEquals(StatusKazne.PLACENA, kazna.getStatusKazne());
        assertEquals(mockVozilo, kazna.getVozilo());
        assertEquals(mockKontrolor, kazna.getKontrolor());
        assertEquals(mockLokacija, kazna.getLokacija());
        assertEquals(mockParkingZona, kazna.getParkingZona());
        assertEquals(mockPlacanje, kazna.getPlacanje());
    }

    @Test
    @DisplayName("Should create Kazna with no-args constructor and have default/null fields")
    void testNoArgsConstructor() {
        Kazna kazna = new Kazna();

        assertNotNull(kazna);
        assertNull(kazna.getId());
        assertNull(kazna.getVremeIzdavanja());
        assertEquals(0.0, kazna.getIznos());
        assertNull(kazna.getStatusKazne());
        assertNull(kazna.getVozilo());
        assertNull(kazna.getKontrolor());
        assertNull(kazna.getLokacija());
        assertNull(kazna.getParkingZona());
        assertNull(kazna.getPlacanje());
    }

    @Test
    @DisplayName("Should correctly assign all associations")
    void testAssociations() {
        Kazna kazna = new Kazna();
        kazna.setVozilo(mockVozilo);
        kazna.setKontrolor(mockKontrolor);
        kazna.setLokacija(mockLokacija);
        kazna.setParkingZona(mockParkingZona);
        kazna.setPlacanje(mockPlacanje);

        assertEquals(mockVozilo, kazna.getVozilo());
        assertEquals(mockKontrolor, kazna.getKontrolor());
        assertEquals(mockLokacija, kazna.getLokacija());
        assertEquals(mockParkingZona, kazna.getParkingZona());
        assertEquals(mockPlacanje, kazna.getPlacanje());
    }
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -500.0, -10.5})
    @DisplayName("Should fail validation when iznos is zero or negative")
    void validate_ZeroOrNegativeIznos_HasViolations(double invalidIznos) {
        Kazna kazna = new Kazna(
                null,
                LocalDateTime.now(),
                invalidIznos,
                StatusKazne.NEPLACENA,
                mockVozilo,
                mockKontrolor,
                mockLokacija,
                mockParkingZona,
                null
        );

        Set<ConstraintViolation<Kazna>> violations = validator.validate(kazna);

        assertFalse(violations.isEmpty(), "Trebalo bi da postoji prekršaj jer iznos mora biti pozitivan");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("iznos")));
    }

    @Test
    @DisplayName("Should fail validation when critical fields are null")
    void validate_NullRequiredFields_HasViolations() {
        Kazna kazna = new Kazna(null, null, 5000.0, null, null, null, null, null, null);

        Set<ConstraintViolation<Kazna>> violations = validator.validate(kazna);

        assertFalse(violations.isEmpty(), "Trebalo bi da postoje prekršaji za null polja");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vremeIzdavanja")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("statusKazne")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vozilo")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("kontrolor")));
    }
}