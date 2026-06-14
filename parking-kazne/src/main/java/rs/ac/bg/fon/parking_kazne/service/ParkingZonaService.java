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
/**
 * Service handling business logic for managing municipal parking zones.
 * Provides capabilities to register new zones, configure tariff rates,
 * and manage zone definitions within the system.
 *
 * @author Lazar Ivosevic
 */
@Slf4j
@Service
@AllArgsConstructor
public class ParkingZonaService {
    private final ParkinZonaRepository parkinZonaRepository;

    /**
     * Creates and registers a new parking zone configuration in the system.
     *
     * @param parkingZonaRequest the DTO containing the zone name, hourly tariff rate, and type classification enum.
     * @return {@link ParkingZonaResponse} representing the newly saved parking zone details.
     */
    @Transactional
    public ParkingZonaResponse create(ParkingZonaRequest parkingZonaRequest){
        ParkingZona parkingZona=new ParkingZona();
        parkingZona.setNaziv(parkingZonaRequest.naziv());
        parkingZona.setCenaPoSatu(parkingZonaRequest.cenaPoSatu());
        parkingZona.setZona(parkingZonaRequest.zona());
        return ParkingZonaResponse.from(parkinZonaRepository.save(parkingZona));
    }
    /**
     * Retrieves all parking zone configurations registered within the system.
     *
     * @return a list of {@link ParkingZonaResponse} objects.
     */
    public List<ParkingZonaResponse> findAll(){
        log.info("Finding all ParkingZona");
        return ParkingZonaResponse.from(parkinZonaRepository.findAll());
    }
    /**
     * Retrieves a specific parking zone by its unique identifier.
     *
     * @param id the unique ID of the parking zone configuration.
     * @return {@link ParkingZonaResponse} representing the requested zone.
     * @throws RuntimeException if no zone matches the provided identifier.
     */
    public ParkingZonaResponse findById(Long id){
        log.info("Finding Parking zona with id: {}",id);
        return ParkingZonaResponse.from(findByInternalId(id));
    }
    /**
     * Internal lookup method to safely fetch a {@link ParkingZona} database entity.
     * Primarily used internally across other application services to eliminate DTO conversion steps.
     *
     * @param id the unique ID of the parking zone configuration.
     * @return the {@link ParkingZona} entity instance.
     * @throws RuntimeException if no configuration records match the given ID.
     */
    public ParkingZona findByInternalId(Long id) {
        Optional<ParkingZona>parkingZona=parkinZonaRepository.findById(id);
        if(parkingZona.isPresent()){
            return parkingZona.get();
        }
        throw new RuntimeException("ParkingZona not found");
    }
    /**
     * Deletes a parking zone configuration from the system by its unique identifier.
     *
     * @param id the unique ID of the parking zone to be removed.
     */
    @Transactional
    public void deleteById(Long id){
        log.info("Delete ParkingZona with id : {}",id);
        findByInternalId(id);
        parkinZonaRepository.deleteById(id);
    }

}
