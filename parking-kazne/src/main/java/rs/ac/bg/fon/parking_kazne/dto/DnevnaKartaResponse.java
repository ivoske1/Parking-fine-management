package rs.ac.bg.fon.parking_kazne.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import rs.ac.bg.fon.parking_kazne.entity.DnevnaKarta;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record DnevnaKartaResponse(Long id, int brojSati, double cena,@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime vremeKupovine,
                                  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime vaziDo, String registracija, Long parkingZonaId) {

    public static DnevnaKartaResponse from(DnevnaKarta dnevnaKarta){
        return new DnevnaKartaResponse(
                dnevnaKarta.getId(),
                dnevnaKarta.getBrojSati(),
                dnevnaKarta.getCena(),
                dnevnaKarta.getVremeKupovine(),
                dnevnaKarta.getVaziDo(),
                dnevnaKarta.getVozilo().getRegistracija(),
                dnevnaKarta.getParkingZona().getId()
        );
    }
    public static List<DnevnaKartaResponse> from(List<DnevnaKarta>lista){
        List<DnevnaKartaResponse> listResponse=new ArrayList<>();
        for(DnevnaKarta dk:lista){
            listResponse.add(from(dk));
        }
        return listResponse;
    }

}
