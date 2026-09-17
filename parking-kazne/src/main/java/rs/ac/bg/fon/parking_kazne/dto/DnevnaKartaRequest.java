package rs.ac.bg.fon.parking_kazne.dto;


public record DnevnaKartaRequest(
        int brojSati,
        String registracija,
        Long parkingZonaId) {
}
