package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.KontrolorRequest;
import rs.ac.bg.fon.parking_kazne.dto.KontrolorResponse;
import rs.ac.bg.fon.parking_kazne.entity.Kontrolor;
import rs.ac.bg.fon.parking_kazne.repository.KontrolorRepository;

import java.util.List;
import java.util.Optional;
/**
 * Service handling business logic for managing parking controllers (officers).
 * Provides capabilities to register new officers, retrieve active profiles,
 * and handle employment records within the system.
 *
 * @author Lazar Ivosevic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KontrolorService {
    private final KontrolorRepository kontrolorRepository;

    /**
     * Registers a new parking controller profile in the system.
     *
     * @param kontrolorRequest the DTO containing the controller's first name, last name, and unique badge number.
     * @return {@link KontrolorResponse} representing the newly saved controller details.
     */
    @Transactional
    public KontrolorResponse create(KontrolorRequest kontrolorRequest){
        Kontrolor kontrolor=new Kontrolor();
        kontrolor.setIme(kontrolorRequest.ime());
        kontrolor.setPrezime(kontrolorRequest.prezime());
        kontrolor.setBrojLegitimacije(kontrolorRequest.brojLegitimacije());

        return KontrolorResponse.from(kontrolorRepository.save(kontrolor));
    }
    /**
     * Retrieves all parking controllers registered within the system.
     *
     * @return a list of {@link KontrolorResponse} objects.
     */
    public List<KontrolorResponse> findAll(){
        log.info("Finding all Kontrolors");
        return KontrolorResponse.from(kontrolorRepository.findAll());
    }
    /**
     * Retrieves a specific parking controller by their unique identifier.
     *
     * @param id the unique ID of the parking controller profile.
     * @return {@link KontrolorResponse} representing the requested officer.
     * @throws RuntimeException if no controller profile matches the provided identifier.
     */
    public KontrolorResponse findById(Long id){
        log.info("Finding kontrolor with id :{}",id);
        return KontrolorResponse.from(findByIdInternal(id));
    }
    /**
     * Internal lookup method to fetch a {@link Kontrolor} database entity safely.
     * Used across external systems or complementary services to bypass object mapping steps.
     *
     * @param id the unique ID of the parking controller.
     * @return the {@link Kontrolor} entity instance.
     * @throws RuntimeException if no officer profile records match the given ID.
     */
    public Kontrolor findByIdInternal(Long id) {
        Optional<Kontrolor>kontrolor=kontrolorRepository.findById(id);
        if(kontrolor.isPresent()){
            return kontrolor.get();
        }
        throw new RuntimeException("Kontrolor not found");
    }
    /**
     * Deletes a parking controller profile from the database by its unique identifier.
     *
     * @param id the unique ID of the controller to be removed.
     */
    @Transactional
    public void deleteById(Long id){
        log.info("Deleting kontrol with id:{}",id);
        findByIdInternal(id);
        kontrolorRepository.deleteById(id);
    }
}
