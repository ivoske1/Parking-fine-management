package rs.ac.bg.fon.parking_kazne.entity;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Set;

class DnevnaKartaTest {
    private Validator validator;
    private Vozilo mockVozilo;
    private ParkingZona mockParkingZona;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockVozilo = Mockito.mock(Vozilo.class);
        mockParkingZona = Mockito.mock(ParkingZona.class);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 100.0",
            "3, 300.0",
            "8, 800.0"
    })
    @DisplayName("Should pass validation with valid DnevnaKarta objects")
    void validate_ValidDnevnaKarta_NoViolations(int brojSati, double cena) {
        LocalDateTime vremeKupovine = LocalDateTime.now();
        LocalDateTime vaziDo = vremeKupovine.plusHours(brojSati);

        DnevnaKarta karta = new DnevnaKarta(null, brojSati, cena, vremeKupovine, vaziDo, mockVozilo, mockParkingZona);

        Set<ConstraintViolation<DnevnaKarta>> violations = validator.validate(karta);

        assertTrue(violations.isEmpty(), "Validna karta ne bi trebalo da ima prekrsaje validacije");
    }

    @Test
    @DisplayName("Should fail validation when brojSati is less than 1")
    void validate_BrojSatiLessThanOne_FailsValidation() {
        DnevnaKarta karta = new DnevnaKarta(null, 0, 150.0, LocalDateTime.now(), LocalDateTime.now().plusHours(1), mockVozilo, mockParkingZona);

        Set<ConstraintViolation<DnevnaKarta>> violations = validator.validate(karta);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        String message = violations.iterator().next().getMessage();
        assertEquals("Broj sati mora biti najmanje 1", message);
    }

    @Test
    @DisplayName("Should fail validation when cena is negative")
    void validate_NegativeCena_FailsValidation() {
        DnevnaKarta karta = new DnevnaKarta(null, 2, -50.0, LocalDateTime.now(), LocalDateTime.now().plusHours(2), mockVozilo, mockParkingZona);

        Set<ConstraintViolation<DnevnaKarta>> violations = validator.validate(karta);

        assertFalse(violations.isEmpty());
        String message = violations.iterator().next().getMessage();
        assertEquals("Cena ne sme biti negativna", message);
    }

    @Test
    @DisplayName("Should fail validation when required associations and timestamps are null")
    void validate_NullFields_FailsValidation() {
        DnevnaKarta karta = new DnevnaKarta(null, 2, 200.0, null, null, null, null);

        Set<ConstraintViolation<DnevnaKarta>> violations = validator.validate(karta);

        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters work correctly")
    void testLombokGettersAndSetters() {
        LocalDateTime vremeKupovine = LocalDateTime.of(2026, 6, 1, 10, 0);
        LocalDateTime vaziDo = LocalDateTime.of(2026, 6, 1, 18, 0);

        Vozilo pravoVozilo = new Vozilo();
        ParkingZona pravaZona = new ParkingZona();

        DnevnaKarta karta = new DnevnaKarta();
        karta.setId(1L);
        karta.setBrojSati(8);
        karta.setCena(800.0);
        karta.setVremeKupovine(vremeKupovine);
        karta.setVaziDo(vaziDo);
        karta.setVozilo(pravoVozilo);
        karta.setParkingZona(pravaZona);

        assertEquals(1L, karta.getId());
        assertEquals(8, karta.getBrojSati());
        assertEquals(800.0, karta.getCena());
        assertEquals(vremeKupovine, karta.getVremeKupovine());
        assertEquals(vaziDo, karta.getVaziDo());
        assertEquals(pravoVozilo, karta.getVozilo());
        assertEquals(pravaZona, karta.getParkingZona());
    }
}