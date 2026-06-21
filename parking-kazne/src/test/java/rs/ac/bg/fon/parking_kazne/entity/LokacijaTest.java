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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LokacijaTest {

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
            "Knez Mihailova, Beograd",
            "Bulevar Oslobodjenja, Novi Sad",
            "Nemanjina, Nis",
            "Trg slobode, Subotica"
    })
    @DisplayName("Should pass validation with valid Lokacija objects")
    void validate_ValidLokacija_NoViolations(String ulica, String grad) {
        Lokacija lokacija = new Lokacija(null, ulica, grad);

        Set<ConstraintViolation<Lokacija>> violations = validator.validate(lokacija);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when ulica is null")
    void validate_NullUlica_HasViolations() {
        Lokacija lokacija = new Lokacija(null, null, "Beograd");

        Set<ConstraintViolation<Lokacija>> violations = validator.validate(lokacija);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("ulica")));
    }

    @Test
    @DisplayName("Should fail validation when grad is null")
    void validate_NullGrad_HasViolations() {
        Lokacija lokacija = new Lokacija(null, "Knez Mihailova", null);

        Set<ConstraintViolation<Lokacija>> violations = validator.validate(lokacija);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("grad")));
    }

    @Test
    @DisplayName("Should fail validation when both fields are null")
    void validate_BothFieldsNull_HasViolations() {
        Lokacija lokacija = new Lokacija(null, null, null);

        Set<ConstraintViolation<Lokacija>> violations = validator.validate(lokacija);

        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        Lokacija lokacija = new Lokacija();

        lokacija.setId(1L);
        lokacija.setUlica("Knez Mihailova");
        lokacija.setGrad("Beograd");

        assertEquals(1L, lokacija.getId());
        assertEquals("Knez Mihailova", lokacija.getUlica());
        assertEquals("Beograd", lokacija.getGrad());
    }

    @Test
    @DisplayName("Should successfully create Lokacija with all-args constructor")
    void testAllArgsConstructor() {
        Lokacija lokacija = new Lokacija(99L, "Nemanjina", "Nis");

        assertNotNull(lokacija);
        assertEquals(99L, lokacija.getId());
        assertEquals("Nemanjina", lokacija.getUlica());
        assertEquals("Nis", lokacija.getGrad());
    }

    @Test
    @DisplayName("Should create Lokacija with no-args constructor and have null fields")
    void testNoArgsConstructor() {
        Lokacija lokacija = new Lokacija();

        assertNotNull(lokacija);
        assertNull(lokacija.getId());
        assertNull(lokacija.getUlica());
        assertNull(lokacija.getGrad());
    }
}