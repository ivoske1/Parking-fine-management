package rs.ac.bg.fon.parking_kazne.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaRequest;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaResponse;
import rs.ac.bg.fon.parking_kazne.entity.Lokacija;
import rs.ac.bg.fon.parking_kazne.repository.LokacijaRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LokacijaService {
    private final LokacijaRepository lokacijaRepository;

    public LokacijaResponse create(LokacijaRequest lokacijaRequest){
        Lokacija lokacija=new Lokacija();
        lokacija.setUlica(lokacijaRequest.ulica());
        lokacija.setGrad(lokacijaRequest.grad());

        System.out.println(lokacija);

        return LokacijaResponse.from(lokacijaRepository.save(lokacija));
    }

    public List<LokacijaResponse> findAll(){
        log.info("Finding all locations");
        return LokacijaResponse.from(lokacijaRepository.findAll());
    }
    public  LokacijaResponse findById(Long id){
        return LokacijaResponse.from(findByIdInternal(id));
    }

    public Lokacija findByIdInternal(Long id){
        Optional<Lokacija> lokacija=lokacijaRepository.findById(id);
        if(lokacija.isPresent()){
            return lokacija.get();
        }
        throw new RuntimeException("Location not found");

    }
    public void deleteById(Long id){
        log.info("Deleting location by id: {}",id);
        findByIdInternal(id);
        lokacijaRepository.deleteById(id);
    }
}
