package rs.ac.bg.fon.parking_kazne.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import rs.ac.bg.fon.parking_kazne.entity.Kazna;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record KaznaResponse(Long id, double iznos,
                            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" )LocalDateTime vremeIzdavanja,
                            StatusKazne status,String registracije,String parkingZona,String imeKontrolera,String ulica
)
{
    public static KaznaResponse from(Kazna kazna){
        return new KaznaResponse(
                kazna.getId(),
                kazna.getIznos(),
                kazna.getVremeIzdavanja(),
                kazna.getStatusKazne(),
                kazna.getVozilo().getRegistracija(),
                kazna.getParkingZona().getNaziv(),
                kazna.getKontrolor().getIme()+" "+kazna.getKontrolor().getPrezime(),

                kazna.getLokacija().getUlica()
        );
    }
    public static List<KaznaResponse> from(List<Kazna>kazne){
        List<KaznaResponse>responseList=new ArrayList<>();
        for(Kazna k:kazne){
            responseList.add(from(k));
        }
        return responseList;
    }


}
