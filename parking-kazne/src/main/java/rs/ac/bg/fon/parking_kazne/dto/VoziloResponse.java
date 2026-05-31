package rs.ac.bg.fon.parking_kazne.dto;

import rs.ac.bg.fon.parking_kazne.entity.Vozac;
import rs.ac.bg.fon.parking_kazne.entity.Vozilo;
import rs.ac.bg.fon.parking_kazne.service.VozacService;

import java.util.ArrayList;
import java.util.List;

public record VoziloResponse(Long id, String registracija, String marka, String model, Long vozacId) {

    public static VoziloResponse from(Vozilo vozilo){
        return new VoziloResponse(
                vozilo.getId(),
                vozilo.getRegistracija(),
                vozilo.getMarka(),
                vozilo.getModel(),
                vozilo.getVozac().getId()
        );
    }
    public static List<VoziloResponse> from(List<Vozilo>vozila){
        List<VoziloResponse>voziloResponses=new ArrayList<>();
        for(Vozilo v: vozila){
            voziloResponses.add(from(v));
        }
        return voziloResponses;
    }

}
