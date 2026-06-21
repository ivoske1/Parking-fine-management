package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaRequest;
import rs.ac.bg.fon.parking_kazne.dto.LokacijaResponse;
import rs.ac.bg.fon.parking_kazne.entity.Lokacija;
import rs.ac.bg.fon.parking_kazne.repository.LokacijaRepository;

import java.util.List;
import java.util.Optional;
/**
 * Service handling business logic for geographic and street location management.
 * Provides operations to add, view, and remove physical address records used across the system.
 *
 * @author Lazar Ivosevic
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LokacijaService {
    private final LokacijaRepository lokacijaRepository;

    /**
     * Creates and stores a new geographical location record in the system.
     *
     * @param lokacijaRequest the DTO containing the street address and city details.
     * @return {@link LokacijaResponse} representing the newly saved location information.
     */
    @Transactional
    public LokacijaResponse create(LokacijaRequest lokacijaRequest){
        Lokacija lokacija=new Lokacija();
        lokacija.setUlica(lokacijaRequest.ulica());
        lokacija.setGrad(lokacijaRequest.grad());

        System.out.println(lokacija);

        return LokacijaResponse.from(lokacijaRepository.save(lokacija));
    }
    /**
     * Retrieves all location records configured within the system.
     *
     * @return a list of {@link LokacijaResponse} objects.
     */
    public List<LokacijaResponse> findAll(){
        log.info("Finding all locations");
        return LokacijaResponse.from(lokacijaRepository.findAll());
    }
    /**
     * Retrieves a specific location by its unique identifier.
     *
     * @param id the unique ID of the target location.
     * @return {@link LokacijaResponse} representing the matching location record.
     * @throws RuntimeException if no location is found with the specified identifier.
     */
    public  LokacijaResponse findById(Long id){
        return LokacijaResponse.from(findByIdInternal(id));
    }
    /**
     * Internal lookup method to fetch a {@link Lokacija} database entity safely.
     * Bypasses DTO mapping wrappers for direct operations within other system services.
     *
     * @param id the unique ID of the location.
     * @return the {@link Lokacija} entity instance.
     * @throws RuntimeException if no location entry matches the given ID.
     */
    public Lokacija findByIdInternal(Long id){
        Optional<Lokacija> lokacija=lokacijaRepository.findById(id);
        if(lokacija.isPresent()){
            return lokacija.get();
        }
        throw new RuntimeException("Location not found");

    }
    /**
     * Deletes a location record from the database by its unique identifier.
     *
     * @param id the unique ID of the location to be removed.
     */
    @Transactional
    public void deleteById(Long id){
        log.info("Deleting location by id: {}",id);
        findByIdInternal(id);
        lokacijaRepository.deleteById(id);
    }
}
