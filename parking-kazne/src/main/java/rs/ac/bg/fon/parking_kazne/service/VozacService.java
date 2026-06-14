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
/**
 * Service handling business logic for driver management.
 * Provides operations for registering, retrieving, and removing drivers within the system.
 *
 * @author Lazar Ivosevic
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VozacService {
    private final VozacRepository vozacRepository;
    /**
     * Registers a new driver profile in the system.
     *
     * @param vozacRequest the DTO containing personal and license identification details of the driver.
     * @return {@link VozacResponse} representing the newly created driver profile.
     */
    @Transactional
    public VozacResponse create(VozacRequest vozacRequest){
        Vozac vozac=new Vozac();
        vozac.setIme(vozacRequest.ime());
        vozac.setPrezime(vozacRequest.prezime());
        vozac.setBrojVozacke(vozacRequest.brojVozacke());
        vozac.setTelefon(vozacRequest.telefon());
        return VozacResponse.from(vozacRepository.save(vozac));
    }
    /**
     * Retrieves all drivers currently registered in the system.
     *
     * @return a list of {@link VozacResponse} objects.
     */
    public List<VozacResponse> findAll(){
        log.info("Finding all Drivers");
        return VozacResponse.from(vozacRepository.findAll());
    }
    /**
     * Retrieves a driver profile by its unique identifier.
     *
     * @param id the unique ID of the driver.
     * @return {@link VozacResponse} representing the requested driver.
     * @throws RuntimeException if the driver profile is not found.
     */
    public  VozacResponse findById(Long id){
        log.info("Finding driver with id: {}",id);
        return VozacResponse.from(findByInternalId(id));
    }
    /**
     * Internal method to retrieve the {@link Vozac} entity.
     * Used across other system services to bypass DTO mapping overhead.
     *
     * @param id the unique ID of the driver.
     * @return the {@link Vozac} entity.
     * @throws RuntimeException if no driver matches the given ID.
     */
    public Vozac findByInternalId(Long id) {
        Optional<Vozac> vozac=vozacRepository.findById(id);
        if(vozac.isPresent()){
            return vozac.get();
        }
        throw new RuntimeException("Driver not found");
    }
    /**
     * Deletes a driver from the system by their unique identifier.
     *
     * @param id the unique ID of the driver to be deleted.
     */
    @Transactional
    public void deleteById(Long id){
        log.info("Deleting driver with id: {}",id);
        findByInternalId(id);
        deleteById(id);
    }

}
