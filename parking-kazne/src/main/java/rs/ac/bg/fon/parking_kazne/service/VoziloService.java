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
/**
 * Service handling business logic for vehicle management.
 * Provides operations for creating, retrieving, and deleting vehicles within the system.
 * * @author Lazar Ivosevic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoziloService {
    private final VozacService vozacService;
    private final VoziloRepository voziloRepository;
    /**
     * Creates a new vehicle record and associates it with a specific driver.
     * * @param voziloRequest the DTO containing vehicle details and the driver's ID.
     * @return {@link VoziloResponse} representing the newly created vehicle.
     */
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
    /**
     * Retrieves all vehicles currently registered in the system.
     * * @return a list of {@link VoziloResponse} objects.
     */
    public List<VoziloResponse> findAll(){
        log.info("Finding all vehicles");
        return VoziloResponse.from(voziloRepository.findAll());
    }

    /**
     * Retrieves a vehicle by its unique identifier.
     * * @param id the unique ID of the vehicle.
     * @return {@link VoziloResponse} representing the requested vehicle.
     * @throws RuntimeException if the vehicle is not found.
     */
    public VoziloResponse findById(Long id){
        log.info("Finding vehicle with id: {}",id);
        return VoziloResponse.from(findByIdInternal(id));
    }
    /**
     * Internal method to retrieve the {@link Vozilo} entity.
     * Used by other service methods to avoid DTO mapping overhead.
     * * @param id the unique ID of the vehicle.
     * @return the {@link Vozilo} entity.
     * @throws RuntimeException if no vehicle matches the given ID.
     */
    public Vozilo findByIdInternal(Long id) {
        Optional<Vozilo> vozilo=voziloRepository.findById(id);
        if(vozilo.isPresent()) {
            return vozilo.get();
        }throw new RuntimeException("Vehicle not found");
    }
    /**
     * Deletes a vehicle from the system by its unique identifier.
     * * @param id the unique ID of the vehicle to be deleted.
     */
    @Transactional
    public void deleteById(Long id){
        log.info("Deleting vehicle with id: {}",id);
        findByIdInternal(id);
        voziloRepository.deleteById(id);
    }
    /**
     * Searches for a vehicle by its license plate registration number.
     * * @param registracija the license plate string to search for (case-insensitive).
     * @return the {@link Vozilo} entity matching the registration.
     * @throws RuntimeException if no vehicle with the given registration is found.
     */
    public Vozilo findByRegistracija(String registracija){
        log.info("Finding vehicle with registration: {}",registracija);
        return voziloRepository.findAll().stream()
                .filter(v->v.getRegistracija().equalsIgnoreCase(registracija))
                .findFirst()
                .orElseThrow(()->new RuntimeException("Not found vehicle"));
    }
}
