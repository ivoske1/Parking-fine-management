package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.ParkingZonaRequest;
import rs.ac.bg.fon.parking_kazne.dto.ParkingZonaResponse;
import rs.ac.bg.fon.parking_kazne.entity.ParkingZona;
import rs.ac.bg.fon.parking_kazne.repository.ParkinZonaRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ParkingZonaService {
    private final ParkinZonaRepository parkinZonaRepository;
    @Transactional
    public ParkingZonaResponse create(ParkingZonaRequest parkingZonaRequest){
        ParkingZona parkingZona=new ParkingZona();
        parkingZona.setNaziv(parkingZonaRequest.naziv());
        parkingZona.setCenaPoSatu(parkingZonaRequest.cenaPoSatu());
        parkingZona.setZona(parkingZonaRequest.zona());
        return ParkingZonaResponse.from(parkinZonaRepository.save(parkingZona));
    }
    public List<ParkingZonaResponse> findAll(){
        log.info("Finding all ParkingZona");
        return ParkingZonaResponse.from(parkinZonaRepository.findAll());
    }
    public ParkingZonaResponse findById(Long id){
        log.info("Finding Parking zona with id: {}",id);
        return ParkingZonaResponse.from(findByInternalId(id));
    }

    public ParkingZona findByInternalId(Long id) {
        Optional<ParkingZona>parkingZona=parkinZonaRepository.findById(id);
        if(parkingZona.isPresent()){
            return parkingZona.get();
        }
        throw new RuntimeException("ParkingZona not found");
    }
    @Transactional
    public void deleteById(Long id){
        log.info("Delete ParkingZona with id : {}",id);
        findByInternalId(id);
        parkinZonaRepository.deleteById(id);
    }

}
