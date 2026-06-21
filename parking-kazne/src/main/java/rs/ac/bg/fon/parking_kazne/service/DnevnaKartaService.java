package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaRequest;
import rs.ac.bg.fon.parking_kazne.dto.DnevnaKartaResponse;
import rs.ac.bg.fon.parking_kazne.entity.DnevnaKarta;
import rs.ac.bg.fon.parking_kazne.entity.ParkingZona;
import rs.ac.bg.fon.parking_kazne.entity.Vozilo;
import rs.ac.bg.fon.parking_kazne.repository.DnevnaKartaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Service handling business logic for issuing and validating daily parking tickets.
 * Coordinates ticket activation records, enforces validation boundaries to prevent overlapping tickets,
 * and handles automated tariff pricing calculations based on parking zone rules.
 * * @author Lazar Ivosevic
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DnevnaKartaService {
    private final DnevnaKartaRepository dnevnaKartaRepository;
    private final VoziloService voziloService;
    private final ParkingZonaService parkingZonaService;

    /**
     * Issues and registers a new daily parking ticket for a specified vehicle.
     * Validates that the vehicle does not currently hold an active, non-expired ticket.
     * Dynamically calculates expiration periods and computing overall billing totals
     * using the corresponding zone's hourly tariff multi-pliers.
     * * @param dnevnaKartaRequest the DTO containing target vehicle identification, zone configuration keys, and requested hours.
     * @return {@link DnevnaKartaResponse} representing the newly created and recorded daily ticket.
     * @throws RuntimeException if the vehicle already possesses an active unexpired ticket,
     * or if dependencies cannot be resolved.
     */
    @Transactional
    public DnevnaKartaResponse create(DnevnaKartaRequest dnevnaKartaRequest){
        log.info("Creating daily ticket :{}",dnevnaKartaRequest);
        List<DnevnaKartaResponse>postojeceKarte=findByRegistracija(dnevnaKartaRequest.registracija());
        LocalDateTime sada=LocalDateTime.now();
        boolean vecIma=postojeceKarte.stream().anyMatch(karta->karta.vaziDo()!=null &&
                karta.vaziDo().isAfter(sada));
        if(vecIma){
            throw new RuntimeException("Vehicle already has a ticket");
        }
        Vozilo v=voziloService.findByRegistracija(dnevnaKartaRequest.registracija());
        ParkingZona pz=parkingZonaService.findByInternalId(dnevnaKartaRequest.parkingZonaId());

        DnevnaKarta dk=new DnevnaKarta();
        dk.setBrojSati(dnevnaKartaRequest.brojSati());
        dk.setVozilo(v);
        dk.setParkingZona(pz);

        dk.setVremeKupovine(LocalDateTime.now());
        dk.setVaziDo(LocalDateTime.now().plusHours(dnevnaKartaRequest.brojSati()));
        dk.setCena(pz.getCenaPoSatu()*dnevnaKartaRequest.brojSati());

        return DnevnaKartaResponse.from(dnevnaKartaRepository.save(dk));
    }
    /**
     * Retrieves all daily parking tickets logged within the system repository.
     * * @return a list of {@link DnevnaKartaResponse} objects.
     */
    public List<DnevnaKartaResponse> findAll(){
        log.info("Finding all daily tickets");
        return DnevnaKartaResponse.from(dnevnaKartaRepository.findAll());
    }
    /**
     * Retrieves a specific daily parking ticket by its unique identifier.
     * * @param id the unique database ID of the target ticket record.
     * @return {@link DnevnaKartaResponse} representing the matched ticket entry.
     * @throws RuntimeException if no daily ticket entry matches the given identifier.
     */
    public DnevnaKartaResponse findById(Long id){
        log.info("Finding daily ticket with id: {}",id);
        return DnevnaKartaResponse.from(findByIdInternal(id));
    }

    /**
     * Internal data access lookup utility to safely fetch a {@link DnevnaKarta} entity instance.
     * Minimizes runtime data mapping transformations for transactional modifications inside the boundary layer.
     * * @param id the unique database ID of the target ticket.
     * @return the raw {@link DnevnaKarta} entity instance.
     * @throws RuntimeException if no ticket records match the provided ID.
     */
    private DnevnaKarta findByIdInternal(Long id) {
        Optional<DnevnaKarta>dnevnaKarta=dnevnaKartaRepository.findById(id);
        if(dnevnaKarta.isPresent()){
            return dnevnaKarta.get();
        }
        throw new RuntimeException("Daily ticket not found");
    }
    /**
     * Remotely removes a daily ticket record from the system data repository using its unique identifier.
     * * @param id the unique ID of the daily ticket slated for removal.
     */
    public void deleteById(Long id){
        log.info("Deleting daily ticket with id:{}",id);
        findByIdInternal(id);
        dnevnaKartaRepository.deleteById(id);
    }
    /**
     * Filters and lists historical and active daily parking tickets issued to a specific vehicle profile.
     * * @param registracija the unique license plate string identifier of the target vehicle.
     * @return a list of {@link DnevnaKartaResponse} tracking records matching the registration.
     */
    public List<DnevnaKartaResponse> findByRegistracija(String registracija){
        List<DnevnaKarta>sveKarte=dnevnaKartaRepository.findAll();
        List<DnevnaKarta>filtrirane=sveKarte.stream().filter(v->v.getVozilo()!=null &&
                v.getVozilo().getRegistracija().equalsIgnoreCase(registracija)).toList();
        return DnevnaKartaResponse.from(filtrirane);
    }

}
