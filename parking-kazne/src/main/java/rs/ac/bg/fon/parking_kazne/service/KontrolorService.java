package rs.ac.bg.fon.parking_kazne.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.KontrolorRequest;
import rs.ac.bg.fon.parking_kazne.dto.KontrolorResponse;
import rs.ac.bg.fon.parking_kazne.entity.Kontrolor;
import rs.ac.bg.fon.parking_kazne.repository.KontrolorRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KontrolorService {
    private final KontrolorRepository kontrolorRepository;

    public KontrolorResponse create(KontrolorRequest kontrolorRequest){
        Kontrolor kontrolor=new Kontrolor();
        kontrolor.setIme(kontrolorRequest.ime());
        kontrolor.setPrezime(kontrolorRequest.prezime());
        kontrolor.setBrojLegitimacije(kontrolorRequest.brojLegitimacije());

        return KontrolorResponse.from(kontrolorRepository.save(kontrolor));
    }
    public List<KontrolorResponse> findAll(){
        log.info("Finding all Kontrolors");
        return KontrolorResponse.from(kontrolorRepository.findAll());
    }
    public KontrolorResponse findById(Long id){
        log.info("Finding kontrolor with id :{}",id);
        return KontrolorResponse.from(findByIdInternal(id));
    }

    public Kontrolor findByIdInternal(Long id) {
        Optional<Kontrolor>kontrolor=kontrolorRepository.findById(id);
        if(kontrolor.isPresent()){
            return kontrolor.get();
        }
        throw new RuntimeException("Kontrolor not found");
    }
    public void deleteById(Long id){
        log.info("Deleting kontrol with id:{}",id);
        findByIdInternal(id);
        kontrolorRepository.deleteById(id);
    }
}
