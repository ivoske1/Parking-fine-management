package rs.ac.bg.fon.parking_kazne.entity;

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
import rs.ac.bg.fon.parking_kazne.enums.Zona;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParkingZonaTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "A1 - Centar, 150.0, CRVENA",
            "B2 - Periferija, 80.0, ZUTA",
            "C3 - Park, 50.0, ZELENA"
    })
    @DisplayName("Should pass validation with valid ParkingZona objects")
    void validate_ValidParkingZona_NoViolations(String naziv, Double cenaPoSatu, String zonaStr) {
        Zona zona = Zona.valueOf(zonaStr);

        ParkingZona parkingZona = new ParkingZona(null, naziv, cenaPoSatu, zona);

        Set<ConstraintViolation<ParkingZona>> violations = validator.validate(parkingZona);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when cenaPoSatu is null")
    void validate_NullCenaPoSatu_ShouldHaveViolations() {
        ParkingZona parkingZona = new ParkingZona(null, "A1 - Centar", null, Zona.CRVENA);

        Set<ConstraintViolation<ParkingZona>> violations = validator.validate(parkingZona);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("cenaPoSatu")));
    }

    @Test
    @DisplayName("Should fail validation when zona is null")
    void validate_NullZona_ShouldHaveViolations() {
        ParkingZona parkingZona = new ParkingZona(null, "B2 - Periferija", 80.0, null);

        Set<ConstraintViolation<ParkingZona>> violations = validator.validate(parkingZona);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("zona")));
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        ParkingZona parkingZona = new ParkingZona();

        parkingZona.setId(1L);
        parkingZona.setNaziv("A1 - Centar");
        parkingZona.setCenaPoSatu(150.0);
        parkingZona.setZona(Zona.CRVENA);

        assertEquals(1L, parkingZona.getId());
        assertEquals("A1 - Centar", parkingZona.getNaziv());
        assertEquals(150.0, parkingZona.getCenaPoSatu());
        assertEquals(Zona.CRVENA, parkingZona.getZona());
    }

    @Test
    @DisplayName("Should successfully create ParkingZona with all-args constructor")
    void testAllArgsConstructor() {
        ParkingZona parkingZona = new ParkingZona(99L, "C3 - Park", 50.0, Zona.ZELENA);

        assertNotNull(parkingZona);
        assertEquals(99L, parkingZona.getId());
        assertEquals("C3 - Park", parkingZona.getNaziv());
        assertEquals(50.0, parkingZona.getCenaPoSatu());
        assertEquals(Zona.ZELENA, parkingZona.getZona());
    }

    @Test
    @DisplayName("Should create ParkingZona with no-args constructor and have null fields")
    void testNoArgsConstructor() {
        ParkingZona parkingZona = new ParkingZona();

        assertNotNull(parkingZona);
        assertNull(parkingZona.getId());
        assertNull(parkingZona.getNaziv());
        assertNull(parkingZona.getCenaPoSatu());
        assertNull(parkingZona.getZona());
    }
}