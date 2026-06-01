package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.VoziloRequest;
import rs.ac.bg.fon.parking_kazne.dto.VoziloResponse;
import rs.ac.bg.fon.parking_kazne.entity.Vozac;
import rs.ac.bg.fon.parking_kazne.entity.Vozilo;
import rs.ac.bg.fon.parking_kazne.repository.VozacRepository;
import rs.ac.bg.fon.parking_kazne.repository.VoziloRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoziloService {
    private final VozacService vozacService;
    private final VoziloRepository voziloRepository;

    @Transactional
    public VoziloResponse create(VoziloRequest voziloRequest){
        Vozilo vozilo=new Vozilo();
        vozilo.setRegistracija(voziloRequest.registracija());
        vozilo.setMarka(voziloRequest.marka());
        vozilo.setModel(voziloRequest.model());
        Vozac vozac=vozacService.findByInternalId(voziloRequest.vozacId());
        vozilo.setVozac(vozac);
        return VoziloResponse.from(voziloRepository.save(vozilo));
    }

    public List<VoziloResponse> findAll(){
        log.info("Finding all vehicles");
        return VoziloResponse.from(voziloRepository.findAll());
    }

    public VoziloResponse findById(Long id){
        log.info("Finding vehicle with id: {}",id);
        return VoziloResponse.from(findByIdInternal(id));
    }

    public Vozilo findByIdInternal(Long id) {
        Optional<Vozilo> vozilo=voziloRepository.findById(id);
        if(vozilo.isPresent()) {
            return vozilo.get();
        }throw new RuntimeException("Vehicle not found");
    }
    @Transactional
    public void deleteById(Long id){
        log.info("Deleting vehicle with id: {}",id);
        findByIdInternal(id);
        deleteById(id);
    }
    public Vozilo findByRegistracija(String registracija){
        log.info("Finding vehicle with registration: {}",registracija);
        return voziloRepository.findAll().stream()
                .filter(v->v.getRegistracija().equalsIgnoreCase(registracija))
                .findFirst()
                .orElseThrow(()->new RuntimeException("Not found vehicle"));
    }
}
