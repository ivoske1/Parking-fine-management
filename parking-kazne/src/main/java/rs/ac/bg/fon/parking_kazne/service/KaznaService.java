package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.KaznaRequest;
import rs.ac.bg.fon.parking_kazne.dto.KaznaResponse;
import rs.ac.bg.fon.parking_kazne.entity.*;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;
import rs.ac.bg.fon.parking_kazne.enums.Zona;
import rs.ac.bg.fon.parking_kazne.repository.KaznaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static rs.ac.bg.fon.parking_kazne.enums.Zona.*;
/**
 * Service handling core business logic for parking citations (fines).
 * Coordinates dependencies across vehicles, controllers, locations, and zones
 * to orchestrate citation lifecycle operations, fine calculations, and filtering.
 * * @author Lazar Ivosevic
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KaznaService {

    private final KaznaRepository kaznaRepository;
    private final VoziloService voziloService;
    private final KontrolorService kontrolorService;
    private final LokacijaService lokacijaService;
    private final ParkingZonaService parkingZonaService;

    /**
     * Issues and registers a new parking citation in the system.
     * Resolves the associated vehicle, officer, location, and parking zone dependencies,
     * verifies that the vehicle does not already carry an unpaid citation,
     * sets the initialization timestamp, automatically computes the violation penalty amount
     * based on zone severity, and defaults the lifecycle status to unpaid.
     * * @param kaznaRequest the DTO containing reference data keys needed to compile the citation.
     * @return {@link KaznaResponse} representing the newly persisted parking citation.
     * @throws RuntimeException if the vehicle already has an unpaid citation, or if any referenced
     * dependency (vehicle, controller, location, zone) cannot be resolved.
     */
    @Transactional
    public KaznaResponse create(KaznaRequest kaznaRequest){
        Vozilo v=voziloService.findByRegistracija(kaznaRequest.registracija());

        boolean vecImaNeplacenu=kaznaRepository.findAll().stream()
                .anyMatch(postojeca->postojeca.getVozilo()!=null
                        && postojeca.getVozilo().getId().equals(v.getId())
                        && postojeca.getStatusKazne()==StatusKazne.NEPLACENA);
        if(vecImaNeplacenu){
            throw new RuntimeException("Vozilo već ima neplaćenu kaznu");
        }

        Kontrolor k=kontrolorService.findByIdInternal(kaznaRequest.kontrolerId());
        Lokacija lok=lokacijaService.findByIdInternal(kaznaRequest.lokacijaId());
        ParkingZona pz=parkingZonaService.findByInternalId(kaznaRequest.parkingZonaId());

        Kazna kazna=new Kazna();
        kazna.setVremeIzdavanja(LocalDateTime.now());
        kazna.setIznos(izracunajIznos(pz.getZona()));
        kazna.setStatusKazne(StatusKazne.NEPLACENA);
        kazna.setVozilo(v);
        kazna.setKontrolor(k);
        kazna.setLokacija(lok);
        kazna.setParkingZona(pz);
        return KaznaResponse.from(kaznaRepository.save(kazna));

    }
    /**
     * Retrieves all parking citations logged in the system database.
     * * @return a list of {@link KaznaResponse} objects.
     */
    public List<KaznaResponse> findAll(){
        log.info("Finding all tickets");
        return KaznaResponse.from(kaznaRepository.findAll());
    }
    /**
     * Retrieves a specific parking citation by its unique database identifier.
     * * @param id the unique ID of the citation.
     * @return {@link KaznaResponse} mapping the matching citation data.
     * @throws RuntimeException if no citation matches the given identifier.
     */
    public KaznaResponse findById(Long id){
        log.info("Finding ticket by id");
        return KaznaResponse.from(findByIdInternal(id));
    }
    /**
     * Internal lookup utility to safely fetch a {@link Kazna} database entity.
     * Enables low-overhead, transactional status mutations and property updates within the service layer.
     * * @param id the unique ID of the citation.
     * @return the raw {@link Kazna} database entity.
     * @throws RuntimeException if no citation records match the given ID.
     */
    public Kazna findByIdInternal(Long id) {
        Optional<Kazna>kazna=kaznaRepository.findById(id);
        if(kazna.isPresent()){
            return kazna.get();
        }
        throw new RuntimeException("Ticket does not exists");
    }
    /**
     * Filters and lists parking citations by their current lifecycle status.
     * * @param statusKazne the target operational status enum to filter by (e.g., NEPLACENA, PLACENA).
     * @return a list of filtered {@link KaznaResponse} instances matching the status requirement.
     */
    public List<KaznaResponse> findByStatus(StatusKazne statusKazne){
        log.info("Finding all tickets :{}",statusKazne);
        List<KaznaResponse>kazne=findAll();
        List<KaznaResponse>filter=kazne.stream().filter(k->k.status()==statusKazne).toList();
        return filter;
    }
    /**
     * Filters and tracks citations issued to a specific vehicle profile.
     * * @param registracija the unique license plate string identifier of the target vehicle.
     * @return a list of {@link KaznaResponse} objects matching the vehicle's records.
     */
    public List<KaznaResponse> findByVozilo(String registracija) {
        log.info("Finding tickets by vozilo: {}", registracija);
        return findAll()
                .stream()
                .filter(k -> k.registracije().equals(registracija))
                .toList();

    }
    /**
     * Updates the systemic lifecycle status of a designated parking citation.
     * Primarily called upon successful payment executions or automated delinquency assessments.
     * * @param id the unique ID of the target citation.
     * @param statusKazne the new state enum to be mapped onto the instance.
     * @return {@link KaznaResponse} detailing the post-mutation citation state.
     */
    @Transactional
    public KaznaResponse updateStatus(Long id,StatusKazne statusKazne){
        log.info("Updating ticket status");
        Kazna k=findByIdInternal(id);
        k.setStatusKazne(statusKazne);
        return KaznaResponse.from(kaznaRepository.save(k));
    }
    /**
     * Remotely discards a citation entry from the system repository by its unique identifier.
     * * @param id the unique ID of the target citation to remove.
     */
    public void deleteById(Long id){
        log.info("Deleting ticket : {}",id);
        findByIdInternal(id);
        kaznaRepository.deleteById(id);
    }

    /**
     * Internal policy engine helper method mapping predefined fine penalty thresholds
     * based on the municipal parking zone zone classification type.
     * * @param zona the specific zone enum category (CRVENA, ZUTA, ZELENA).
     * @return the calculated static penalty cost as a double precision decimal value.
     */
    private double izracunajIznos(Zona zona) {
        return switch (zona) {
            case CRVENA -> 5000;
            case ZUTA -> 3000;
            case ZELENA -> 1000;
        };
    }
}
