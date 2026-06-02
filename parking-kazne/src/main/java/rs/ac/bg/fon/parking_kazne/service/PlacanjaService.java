package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeRequest;
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeResponse;
import rs.ac.bg.fon.parking_kazne.entity.Kazna;
import rs.ac.bg.fon.parking_kazne.entity.Placanje;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;
import rs.ac.bg.fon.parking_kazne.repository.PlacanjeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlacanjaService {
    private final KaznaService kaznaService;
    private final PlacanjeRepository placanjeRepository;


    @Transactional
    public  PlacanjeResponse create(PlacanjeRequest placanjeRequest){
        log.info("Creating new payment");
        Kazna kazna=kaznaService.findByIdInternal(placanjeRequest.kaznaId());
        if(kazna.getStatusKazne()== StatusKazne.PLACENA){
            throw new RuntimeException("Ticket is already payed");
        }
        if(placanjeRequest.iznos()<kazna.getIznos()){
            throw new RuntimeException("Invalid payment");
        }
        kaznaService.updateStatus(kazna.getId(),StatusKazne.PLACENA);

        Placanje novoPlacanje=new Placanje();
        novoPlacanje.setKazna(kazna);
        novoPlacanje.setIznos(placanjeRequest.iznos());
        novoPlacanje.setDatum(LocalDate.now());
        novoPlacanje.setNacinPlacanja(placanjeRequest.nacin());

        Placanje sacuvanoPlacanje=placanjeRepository.save(novoPlacanje);
        return PlacanjeResponse.from(sacuvanoPlacanje);
    }
    public List<PlacanjeResponse> findAll(){
        log.info("Finding all payments");
        return PlacanjeResponse.from(placanjeRepository.findAll());
    }
    public PlacanjeResponse findById(Long id){
        log.info("Finding payment");
        return PlacanjeResponse.from(findByIdInternal(id));
    }

    private Placanje findByIdInternal(Long id) {
        Optional<Placanje> placanje=placanjeRepository.findById(id);
        if(placanje.isPresent()){
            return placanje.get();
        }throw new RuntimeException("No payment in system");
    }
}
