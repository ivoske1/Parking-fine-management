package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaRequest;
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaResponse;
import rs.ac.bg.fon.parking_kazne.entity.DnevnaKarta;
import rs.ac.bg.fon.parking_kazne.entity.ParkingZona;
import rs.ac.bg.fon.parking_kazne.entity.Vozilo;
import rs.ac.bg.fon.parking_kazne.repository.DnevnaKartaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DnevnaKartaService {
    private final DnevnaKartaRepository dnevnaKartaRepository;
    private final VoziloService voziloService;
    private final ParkingZonaService parkingZonaService;


    @Transactional
    public DnevnaKartaResponse create(DnevnaKartaRequest dnevnaKartaRequest){
        log.info("Creating daily ticket :{}",dnevnaKartaRequest);
        List<DnevnaKartaResponse>postojeceKarte=findByRegistracija(dnevnaKartaRequest.registracija());
        LocalDateTime sada=LocalDateTime.now();
        boolean vecIma=postojeceKarte.stream().anyMatch(karta->karta.vaziDo()!=null &&
                karta.vaziDo().isAfter(sada));
        if(vecIma){
            throw new RuntimeException("Vehicle already has a ticket");
        }
        Vozilo v=voziloService.findByRegistracija(dnevnaKartaRequest.registracija());
        ParkingZona pz=parkingZonaService.findByInternalId(dnevnaKartaRequest.parkingZonaId());

        DnevnaKarta dk=new DnevnaKarta();
        dk.setBrojSati(dnevnaKartaRequest.brojSati());
        dk.setVozilo(v);
        dk.setParkingZona(pz);

        dk.setVremeKupovine(LocalDateTime.now());
        dk.setVaziDo(LocalDateTime.now().plusHours(dnevnaKartaRequest.brojSati()));
        dk.setCena(pz.getCenaPoSatu()*dnevnaKartaRequest.brojSati());

        return DnevnaKartaResponse.from(dnevnaKartaRepository.save(dk));
    }
    public List<DnevnaKartaResponse> findAll(){
        log.info("Finding all daily tickets");
        return DnevnaKartaResponse.from(dnevnaKartaRepository.findAll());
    }
    public DnevnaKartaResponse findById(Long id){
        log.info("Finding daily ticket with id: {}",id);
        return DnevnaKartaResponse.from(findByIdInternal(id));
    }

    private DnevnaKarta findByIdInternal(Long id) {
        Optional<DnevnaKarta>dnevnaKarta=dnevnaKartaRepository.findById(id);
        if(dnevnaKarta.isPresent()){
            return dnevnaKarta.get();
        }
        throw new RuntimeException("Daily ticket not found");
    }
    public void deleteById(Long id){
        log.info("Deleting daily ticket with id:{}",id);
        findByIdInternal(id);
        dnevnaKartaRepository.deleteById(id);
    }
    public List<DnevnaKartaResponse> findByRegistracija(String registracija){
        List<DnevnaKarta>sveKarte=dnevnaKartaRepository.findAll();
        List<DnevnaKarta>filtrirane=sveKarte.stream().filter(v->v.getVozilo()!=null &&
                v.getVozilo().getRegistracija().equalsIgnoreCase(registracija)).toList();
        return DnevnaKartaResponse.from(filtrirane);
    }

}
