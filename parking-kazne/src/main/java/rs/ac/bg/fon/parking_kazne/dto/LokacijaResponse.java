package rs.ac.bg.fon.parking_kazne.dto;

import rs.ac.bg.fon.parking_kazne.entity.Lokacija;

import java.util.ArrayList;
import java.util.List;

public record LokacijaResponse(Long id, String ulica, String grad) {
    public static LokacijaResponse from (Lokacija lokacija){
        return new LokacijaResponse(
                lokacija.getId(),
                lokacija.getUlica(),
                lokacija.getGrad()
        );
    }
    public static List<LokacijaResponse> from(List<Lokacija> lokacije) {
        List<LokacijaResponse> listResponse = new ArrayList<>();
        for (Lokacija lokacija : lokacije) {
            listResponse.add(from(lokacija));
        }
        return listResponse;
    }
}
