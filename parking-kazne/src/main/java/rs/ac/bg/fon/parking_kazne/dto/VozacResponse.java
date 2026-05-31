package rs.ac.bg.fon.parking_kazne.dto;

import rs.ac.bg.fon.parking_kazne.entity.Vozac;

import java.util.ArrayList;
import java.util.List;

public record VozacResponse(Long id, String ime, String prezime, String brojVozacke, String telefon) {


    public static VozacResponse from(Vozac vozac){
        return new VozacResponse(
                vozac.getId(),
                vozac.getIme(),
                vozac.getPrezime(),
                vozac.getBrojVozacke(),
                vozac.getTelefon()
        );
    }
    public static List<VozacResponse> from(List<Vozac>vozaci){
        List<VozacResponse>vozacResponses=new ArrayList<>();
        for(Vozac v: vozaci){
            vozacResponses.add(from(v));
        }
        return vozacResponses;
    }
}
