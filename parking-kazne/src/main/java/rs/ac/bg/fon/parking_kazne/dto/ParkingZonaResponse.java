package rs.ac.bg.fon.parking_kazne.dto;

import rs.ac.bg.fon.parking_kazne.entity.ParkingZona;
import rs.ac.bg.fon.parking_kazne.enums.Zona;

import java.util.ArrayList;
import java.util.List;

public record ParkingZonaResponse(Long id, String naziv, Double cenaPoSatu, Zona zona) {

    public static ParkingZonaResponse from(ParkingZona parkingZona){
        return new ParkingZonaResponse(
                parkingZona.getId(),
                parkingZona.getNaziv(),
                parkingZona.getCenaPoSatu(),
                parkingZona.getZona()
        );
    }
    public static List<ParkingZonaResponse>from(List<ParkingZona>lista){
        List<ParkingZonaResponse>listaResponse=new ArrayList<>();
        for(ParkingZona p: lista){
            listaResponse.add(from(p));
        }
        return listaResponse;
    }
}
