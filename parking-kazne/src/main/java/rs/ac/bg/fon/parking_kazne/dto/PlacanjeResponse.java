package rs.ac.bg.fon.parking_kazne.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import rs.ac.bg.fon.parking_kazne.entity.Placanje;
import rs.ac.bg.fon.parking_kazne.enums.NacinPlacanja;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record PlacanjeResponse(Long id, Long kaznaId, double iznos,
                               @JsonFormat(pattern = "yyyy-MM-dd")LocalDate datum,
                               NacinPlacanja nacin, StatusKazne status) {

    public static PlacanjeResponse from(Placanje placanje){
        return new PlacanjeResponse(
                placanje.getId(),
                placanje.getKazna().getId(),
                placanje.getIznos(),
                placanje.getDatum(),
                placanje.getNacinPlacanja(),
                placanje.getKazna().getStatusKazne()
        );
    }
    public static List<PlacanjeResponse> from(List<Placanje>placanja){
        List<PlacanjeResponse>svaPlacanja=new ArrayList<>();
        for(Placanje p:placanja){
            svaPlacanja.add(from(p));
        }return svaPlacanja;
    }
}
