package rs.ac.bg.fon.parking_kazne.dto;

import rs.ac.bg.fon.parking_kazne.enums.Zona;

public record ParkingZonaRequest(String naziv, Double cenaPoSatu, Zona zona) {

}
