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
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

class KontrolorTest {
    private Validator validator;
    private List<Kazna> mockKazne;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockKazne = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Nikola, Nikolic, LEG-001",
            "Jovana, Jovanovic, LEG-002",
            "Stefan, Stefanovic, LEG-003"
    })
    @DisplayName("Should pass validation with valid Kontrolor objects")
    void validate_ValidKontrolors_NoViolations(String ime, String prezime, String brojLegitimacije) {
        Kontrolor kontrolor = new Kontrolor(null, ime, prezime, brojLegitimacije, mockKazne);

        Set<ConstraintViolation<Kontrolor>> violations = validator.validate(kontrolor);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        Kontrolor kontrolor = new Kontrolor();

        kontrolor.setId(1L);
        kontrolor.setIme("Nikola");
        kontrolor.setPrezime("Nikolic");
        kontrolor.setBrojLegitimacije("LEG-001");
        kontrolor.setKazne(mockKazne);

        assertEquals(1L, kontrolor.getId());
        assertEquals("Nikola", kontrolor.getIme());
        assertEquals("Nikolic", kontrolor.getPrezime());
        assertEquals("LEG-001", kontrolor.getBrojLegitimacije());
        assertEquals(mockKazne, kontrolor.getKazne());
    }

    @Test
    @DisplayName("Should successfully create Kontrolor with all-args constructor")
    void testAllArgsConstructor() {
        Kontrolor kontrolor = new Kontrolor(99L, "Jovana", "Jovanovic", "LEG-002", mockKazne);

        assertNotNull(kontrolor);
        assertEquals(99L, kontrolor.getId());
        assertEquals("Jovana", kontrolor.getIme());
        assertEquals("Jovanovic", kontrolor.getPrezime());
        assertEquals("LEG-002", kontrolor.getBrojLegitimacije());
        assertEquals(mockKazne, kontrolor.getKazne());
    }

    @Test
    @DisplayName("Should create Kontrolor with no-args constructor and have null fields")
    void testNoArgsConstructor() {
        Kontrolor kontrolor = new Kontrolor();

        assertNotNull(kontrolor);
        assertNull(kontrolor.getId());
        assertNull(kontrolor.getIme());
        assertNull(kontrolor.getPrezime());
        assertNull(kontrolor.getBrojLegitimacije());
        assertNull(kontrolor.getKazne());
    }

    @Test
    @DisplayName("Should correctly assign kazne list to kontrolor")
    void testKazneAssociation() {
        Kontrolor kontrolor = new Kontrolor();
        kontrolor.setKazne(mockKazne);

        assertEquals(mockKazne, kontrolor.getKazne());
    }
    @Test
    @DisplayName("Should fail validation when required fields are null")
    void validate_NullRequiredFields_HasViolations() {
        Kontrolor kontrolor = new Kontrolor(null, null, null, null, mockKazne);

        Set<ConstraintViolation<Kontrolor>> violations = validator.validate(kontrolor);

        assertFalse(violations.isEmpty(), "Trebalo bi da postoje prekršaji jer su polja null");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ime")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("prezime")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("brojLegitimacije")));
    }
}