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
class VoziloTest {
    private Validator validator;
    private Vozac mockVozac;
    private List<Kazna> mockKazne;
    private List<DnevnaKarta> mockDnevneKarte;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockVozac = Mockito.mock(Vozac.class);
        mockKazne = Mockito.mock(List.class);
        mockDnevneKarte = Mockito.mock(List.class);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "BG-123-XX, Fiat, Punto",
            "NS-456-AB, Volkswagen, Golf",
            "NI-789-CD, BMW, 320d"
    })
    @DisplayName("Should pass validation with valid Vozilo objects")
    void validate_ValidVozilo_NoViolations(String registracija, String marka, String model) {
        Vozilo vozilo = new Vozilo(null, registracija, marka, model, mockVozac, mockKazne, mockDnevneKarte);

        Set<ConstraintViolation<Vozilo>> violations = validator.validate(vozilo);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        Vozilo vozilo = new Vozilo();

        vozilo.setId(1L);
        vozilo.setRegistracija("BG-123-XX");
        vozilo.setMarka("Fiat");
        vozilo.setModel("Punto");
        vozilo.setVozac(mockVozac);
        vozilo.setKazne(mockKazne);
        vozilo.setDnevneKarte(mockDnevneKarte);

        assertEquals(1L, vozilo.getId());
        assertEquals("BG-123-XX", vozilo.getRegistracija());
        assertEquals("Fiat", vozilo.getMarka());
        assertEquals("Punto", vozilo.getModel());
        assertEquals(mockVozac, vozilo.getVozac());
        assertEquals(mockKazne, vozilo.getKazne());
        assertEquals(mockDnevneKarte, vozilo.getDnevneKarte());
    }

    @Test
    @DisplayName("Should successfully create Vozilo with all-args constructor")
    void testAllArgsConstructor() {
        Vozilo vozilo = new Vozilo(99L, "BG-123-XX", "Fiat", "Punto", mockVozac, mockKazne, mockDnevneKarte);

        assertNotNull(vozilo);
        assertEquals(99L, vozilo.getId());
        assertEquals("BG-123-XX", vozilo.getRegistracija());
        assertEquals("Fiat", vozilo.getMarka());
        assertEquals("Punto", vozilo.getModel());
        assertEquals(mockVozac, vozilo.getVozac());
        assertEquals(mockKazne, vozilo.getKazne());
        assertEquals(mockDnevneKarte, vozilo.getDnevneKarte());
    }

    @Test
    @DisplayName("Should create Vozilo with no-args constructor and have null fields")
    void testNoArgsConstructor() {
        Vozilo vozilo = new Vozilo();

        assertNotNull(vozilo);
        assertNull(vozilo.getId());
        assertNull(vozilo.getRegistracija());
        assertNull(vozilo.getMarka());
        assertNull(vozilo.getModel());
        assertNull(vozilo.getVozac());
        assertNull(vozilo.getKazne());
        assertNull(vozilo.getDnevneKarte());
    }

    @Test
    @DisplayName("Should correctly assign vozac to vozilo")
    void testVozacAssociation() {
        Vozilo vozilo = new Vozilo();
        vozilo.setVozac(mockVozac);

        assertEquals(mockVozac, vozilo.getVozac());
    }

    @Test
    @DisplayName("Should correctly assign kazne and dnevneKarte lists to vozilo")
    void testListAssociations() {
        Vozilo vozilo = new Vozilo();
        vozilo.setKazne(mockKazne);
        vozilo.setDnevneKarte(mockDnevneKarte);

        assertEquals(mockKazne, vozilo.getKazne());
        assertEquals(mockDnevneKarte, vozilo.getDnevneKarte());
    }
    @Test
    @DisplayName("Should fail validation when required fields are null")
    void validate_NullRequiredFields_HasViolations() {
        Vozilo vozilo = new Vozilo(null, null, null, null, null, mockKazne, mockDnevneKarte);

        Set<ConstraintViolation<Vozilo>> violations = validator.validate(vozilo);

        assertFalse(violations.isEmpty(), "Trebalo bi da postoje prekršaji jer su obavezna polja null");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("registracija")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("marka")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("model")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("vozac")));
    }
}