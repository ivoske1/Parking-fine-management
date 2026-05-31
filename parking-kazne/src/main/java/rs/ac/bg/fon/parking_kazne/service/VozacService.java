package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.VozacRequest;
import rs.ac.bg.fon.parking_kazne.dto.VozacResponse;
import rs.ac.bg.fon.parking_kazne.entity.Vozac;
import rs.ac.bg.fon.parking_kazne.repository.VozacRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class VozacService {
    private final VozacRepository vozacRepository;
    @Transactional
    public VozacResponse create(VozacRequest vozacRequest){
        Vozac vozac=new Vozac();
        vozac.setIme(vozacRequest.ime());
        vozac.setPrezime(vozacRequest.prezime());
        vozac.setBrojVozacke(vozacRequest.brojVozacke());
        vozac.setTelefon(vozacRequest.telefon());
        return VozacResponse.from(vozacRepository.save(vozac));
    }

    public List<VozacResponse> findAll(){
        log.info("Finding all Drivers");
        return VozacResponse.from(vozacRepository.findAll());
    }

    public  VozacResponse findById(Long id){
        log.info("Finding driver with id: {}",id);
        return VozacResponse.from(findByInternalId(id));
    }

    public Vozac findByInternalId(Long id) {
        Optional<Vozac> vozac=vozacRepository.findById(id);
        if(vozac.isPresent()){
            return vozac.get();
        }
        throw new RuntimeException("Driver not found");
    }
    @Transactional
    public void deleteById(Long id){
        log.info("Deleting driver with id: {}",id);
        findByInternalId(id);
        deleteById(id);
    }

}
