package rs.ac.bg.fon.parking_kazne.dto;

import java.time.LocalDateTime;

public record DnevnaKartaRequest(int brojSati,String registracija,Long parkingZonaId) {
}
