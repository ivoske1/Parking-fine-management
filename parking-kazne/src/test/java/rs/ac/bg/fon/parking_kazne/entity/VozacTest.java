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
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VozacTest {
    private Validator validator;
    private List<Vozilo> mockVozila;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockVozila = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "Petar, Petrovic, VP123456, 0641234567",
            "Marko, Markovic, MP654321, 0697654321",
            "Ana, Anic, AP111222, 0611112222"
    })
    @DisplayName("Should pass validation with valid Vozac objects")
    void validate_ValidVozac_NoViolations(String ime, String prezime, String brojVozacke, String telefon) {
        Vozac vozac = new Vozac(null, ime, prezime, brojVozacke, telefon, mockVozila);

        Set<ConstraintViolation<Vozac>> violations = validator.validate(vozac);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        Vozac vozac = new Vozac();

        vozac.setId(1L);
        vozac.setIme("Petar");
        vozac.setPrezime("Petrovic");
        vozac.setBrojVozacke("VP123456");
        vozac.setTelefon("0641234567");
        vozac.setVozila(mockVozila);

        assertEquals(1L, vozac.getId());
        assertEquals("Petar", vozac.getIme());
        assertEquals("Petrovic", vozac.getPrezime());
        assertEquals("VP123456", vozac.getBrojVozacke());
        assertEquals("0641234567", vozac.getTelefon());
        assertEquals(mockVozila, vozac.getVozila());
    }

    @Test
    @DisplayName("Should successfully create Vozac with all-args constructor")
    void testAllArgsConstructor() {
        Vozac vozac = new Vozac(99L, "Ana", "Nikolic", "AP111222", "0611112222", mockVozila);

        assertNotNull(vozac);
        assertEquals(99L, vozac.getId());
        assertEquals("Ana", vozac.getIme());
        assertEquals("Nikolic", vozac.getPrezime());
        assertEquals("AP111222", vozac.getBrojVozacke());
        assertEquals("0611112222", vozac.getTelefon());
        assertEquals(mockVozila, vozac.getVozila());
    }

    @Test
    @DisplayName("Should create Vozac with no-args constructor and have null fields")
    void testNoArgsConstructor() {
        Vozac vozac = new Vozac();

        assertNotNull(vozac);
        assertNull(vozac.getIme());
        assertNull(vozac.getPrezime());
        assertNull(vozac.getBrojVozacke());
        assertNull(vozac.getTelefon());
        assertNull(vozac.getVozila());
    }

    @Test
    @DisplayName("Should correctly assign vozila list to vozac")
    void testVozilaAssociation() {
        Vozac vozac = new Vozac();
        vozac.setVozila(mockVozila);

        assertEquals(mockVozila, vozac.getVozila());
    }
    @Test
    @DisplayName("Should fail validation when required fields are null")
    void validate_NullRequiredFields_HasViolations() {
        Vozac vozac = new Vozac(null, null, null, null, null, mockVozila);

        Set<ConstraintViolation<Vozac>> violations = validator.validate(vozac);

        assertFalse(violations.isEmpty(), "Trebalo bi da postoje prekršaji jer su obavezna polja null");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("ime")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("prezime")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("brojVozacke")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("telefon")));
    }


}