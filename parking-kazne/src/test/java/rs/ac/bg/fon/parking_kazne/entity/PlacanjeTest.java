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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import rs.ac.bg.fon.parking_kazne.enums.NacinPlacanja;

import java.time.LocalDate;
import java.util.Set;

class PlacanjeTest {
    private Validator validator;
    private Kazna mockKazna;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockKazna = Mockito.mock(Kazna.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "5000.0, 2025-06-01, KARTICA",
            "3000.0, 2025-05-15, GOTOVINA",
            "1500.0, 2025-04-20, ONLINE"
    })
    @DisplayName("Should pass validation with valid Placanje objects")
    void validate_ValidPlacanje_NoViolations(double iznos, LocalDate datum, String nacinPlacanjaStr) {
        NacinPlacanja nacinPlacanja = NacinPlacanja.valueOf(nacinPlacanjaStr);

        Placanje placanje = new Placanje(null, iznos, datum, nacinPlacanja, mockKazna);

        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        LocalDate datum = LocalDate.of(2025, 6, 1);

        Placanje placanje = new Placanje();
        placanje.setId(1L);
        placanje.setIznos(5000.0);
        placanje.setDatum(datum);
        placanje.setNacinPlacanja(NacinPlacanja.KARTICA);
        placanje.setKazna(mockKazna);

        assertEquals(1L, placanje.getId());
        assertEquals(5000.0, placanje.getIznos());
        assertEquals(datum, placanje.getDatum());
        assertEquals(NacinPlacanja.KARTICA, placanje.getNacinPlacanja());
        assertEquals(mockKazna, placanje.getKazna());
    }

    @Test
    @DisplayName("Should successfully create Placanje with all-args constructor")
    void testAllArgsConstructor() {
        LocalDate datum = LocalDate.of(2025, 5, 15);

        Placanje placanje = new Placanje(99L, 3000.0, datum, NacinPlacanja.GOTOVINA, mockKazna);

        assertNotNull(placanje);
        assertEquals(99L, placanje.getId());
        assertEquals(3000.0, placanje.getIznos());
        assertEquals(datum, placanje.getDatum());
        assertEquals(NacinPlacanja.GOTOVINA, placanje.getNacinPlacanja());
        assertEquals(mockKazna, placanje.getKazna());
    }

    @Test
    @DisplayName("Should create Placanje with no-args constructor and have default/null fields")
    void testNoArgsConstructor() {
        Placanje placanje = new Placanje();

        assertNotNull(placanje);
        assertNull(placanje.getId());
        assertEquals(0.0, placanje.getIznos());
        assertNull(placanje.getDatum());
        assertNull(placanje.getNacinPlacanja());
        assertNull(placanje.getKazna());
    }

    @Test
    @DisplayName("Should correctly assign kazna association")
    void testKaznaAssociation() {
        Placanje placanje = new Placanje();
        placanje.setKazna(mockKazna);

        assertEquals(mockKazna, placanje.getKazna());
    }

    @Test
    @DisplayName("Should verify all NacinPlacanja enum values are valid")
    void testSvihNacinaPlacanja() {
        for (NacinPlacanja nacin : NacinPlacanja.values()) {
            Placanje placanje = new Placanje(null, 1000.0, LocalDate.now(), nacin, mockKazna);

            Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);

            assertTrue(violations.isEmpty(), "Ocekivano bez violations za: " + nacin);
        }
    }
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -150.0, -5000.50})
    @DisplayName("Should fail validation when iznos is zero or negative")
    void validate_ZeroOrNegativeIznos_HasViolations(double invalidIznos) {
        Placanje placanje = new Placanje(
                null,
                invalidIznos,
                LocalDate.now(),
                NacinPlacanja.ONLINE,
                mockKazna
        );

        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);

        assertFalse(violations.isEmpty(), "Trebalo bi da postoji prekršaj jer iznos mora biti pozitivan");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("iznos")));
    }

    @Test
    @DisplayName("Should fail validation when required fields are null")
    void validate_NullRequiredFields_HasViolations() {
        Placanje placanje = new Placanje(null, 5000.0, null, null, null);

        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);

        assertFalse(violations.isEmpty(), "Trebalo bi da postoje prekršaji za null polja");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("datum")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nacinPlacanja")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("kazna")));
    }
}