package rs.ac.bg.fon.parking_kazne.dto;

import rs.ac.bg.fon.parking_kazne.entity.Kontrolor;

import java.util.ArrayList;
import java.util.List;

public record KontrolorResponse(Long id, String ime, String prezime, String brojLegitimacije) {

    public static KontrolorResponse from(Kontrolor kontrolor){
        return new KontrolorResponse(
                kontrolor.getId(),
                kontrolor.getIme(),
                kontrolor.getPrezime(),
                kontrolor.getBrojLegitimacije()
        );
    }
    public static List<KontrolorResponse>from(List<Kontrolor>lista){
        List<KontrolorResponse> listaResponse=new ArrayList<>();
        for(Kontrolor k:lista){
            listaResponse.add(from(k));
        }
        return listaResponse;
    }

}
