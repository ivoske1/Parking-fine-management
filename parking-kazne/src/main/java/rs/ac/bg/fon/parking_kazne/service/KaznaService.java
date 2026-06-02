package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.KaznaRequest;
import rs.ac.bg.fon.parking_kazne.dto.KaznaResponse;
import rs.ac.bg.fon.parking_kazne.entity.*;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;
import rs.ac.bg.fon.parking_kazne.enums.Zona;
import rs.ac.bg.fon.parking_kazne.repository.KaznaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static rs.ac.bg.fon.parking_kazne.enums.Zona.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KaznaService {

    private final KaznaRepository kaznaRepository;
    private final VoziloService voziloService;
    private final KontrolorService kontrolorService;
    private final LokacijaService lokacijaService;
    private final ParkingZonaService parkingZonaService;


    @Transactional
    public KaznaResponse create(KaznaRequest kaznaRequest){
        Vozilo v=voziloService.findByRegistracija(kaznaRequest.registracija());
        Kontrolor k=kontrolorService.findByIdInternal(kaznaRequest.kontrolerId());
        Lokacija lok=lokacijaService.findByIdInternal(kaznaRequest.lokacijaId());
        ParkingZona pz=parkingZonaService.findByInternalId(kaznaRequest.parkingZonaId());

        Kazna kazna=new Kazna();
        kazna.setVremeIzdavanja(LocalDateTime.now());
        kazna.setIznos(izracunajIznos(pz.getZona()));
        kazna.setStatusKazne(StatusKazne.NEPLACENA);
        kazna.setVozilo(v);
        kazna.setKontrolor(k);
        kazna.setLokacija(lok);
        kazna.setParkingZona(pz);
        return KaznaResponse.from(kaznaRepository.save(kazna));

    }
    public List<KaznaResponse> findAll(){
        log.info("Finding all tickets");
        return KaznaResponse.from(kaznaRepository.findAll());
    }
    public KaznaResponse findById(Long id){
        log.info("Finding ticket by id");
        return KaznaResponse.from(findByIdInternal(id));
    }

    public Kazna findByIdInternal(Long id) {
        Optional<Kazna>kazna=kaznaRepository.findById(id);
        if(kazna.isPresent()){
            return kazna.get();
        }
        throw new RuntimeException("Ticket does not exists");
    }
    public List<KaznaResponse> findByStatus(StatusKazne statusKazne){
        log.info("Finding all tickets :{}",statusKazne);
        List<KaznaResponse>kazne=findAll();
        List<KaznaResponse>filter=kazne.stream().filter(k->k.status()==statusKazne).toList();
        return filter;
    }
    public List<KaznaResponse> findByVozilo(String registracija) {
        log.info("Finding tickets by vozilo: {}", registracija);
        return findAll()
                .stream()
                .filter(k -> k.registracije().equals(registracija))
                .toList();

    }
    @Transactional
    public KaznaResponse updateStatus(Long id,StatusKazne statusKazne){
        log.info("Updating ticket status");
        Kazna k=findByIdInternal(id);
        k.setStatusKazne(statusKazne);
        return KaznaResponse.from(kaznaRepository.save(k));
    }

    public void deleteById(Long id){
        log.info("Deleting ticket : {}",id);
        findByIdInternal(id);
        kaznaRepository.deleteById(id);
    }


    private double izracunajIznos(Zona zona) {
        return switch (zona) {
            case CRVENA -> 5000;
            case ZUTA -> 3000;
            case ZELENA -> 1000;
        };
    }
}
