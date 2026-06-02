package rs.ac.bg.fon.parking_kazne.dto;

import rs.ac.bg.fon.parking_kazne.enums.NacinPlacanja;

public record PlacanjeRequest(Long kaznaId, double iznos, NacinPlacanja nacin) {
}
