package rs.ac.bg.fon.parking_kazne.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeRequest;
import rs.ac.bg.fon.parking_kazne.dto.PlacanjeResponse;
import rs.ac.bg.fon.parking_kazne.entity.Kazna;
import rs.ac.bg.fon.parking_kazne.entity.Placanje;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;
import rs.ac.bg.fon.parking_kazne.repository.PlacanjeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 * Service handling business logic for processing and tracking citation payments.
 * Manages transactional flows, validates payment conditions, and updates citation lifecycles.
 *
 * @author Lazar Ivosevic
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlacanjaService {
    private final KaznaService kaznaService;
    private final PlacanjeRepository placanjeRepository;

    /**
     * Processes and records a new payment for a specific parking citation.
     * Validates that the citation is not already paid and that the processed amount
     * is sufficient to cover the citation balance before updating the citation status.
     *
     * @param placanjeRequest the DTO containing the target citation ID, payment amount, and payment method channel.
     * @return {@link PlacanjeResponse} representing the finalized and persisted payment record.
     * @throws RuntimeException if the citation is already settled, if the payment amount is less than the required fine,
     *                          or if the citation does not exist.
     */
    @Transactional
    public  PlacanjeResponse create(PlacanjeRequest placanjeRequest){
        log.info("Creating new payment");
        Kazna kazna=kaznaService.findByIdInternal(placanjeRequest.kaznaId());
        if(kazna.getStatusKazne()== StatusKazne.PLACENA){
            throw new RuntimeException("Ticket is already payed");
        }
        if(placanjeRequest.iznos()<kazna.getIznos()){
            throw new RuntimeException("Invalid payment");
        }
        kaznaService.updateStatus(kazna.getId(),StatusKazne.PLACENA);

        Placanje novoPlacanje=new Placanje();
        novoPlacanje.setKazna(kazna);
        novoPlacanje.setIznos(placanjeRequest.iznos());
        novoPlacanje.setDatum(LocalDate.now());
        novoPlacanje.setNacinPlacanja(placanjeRequest.nacin());

        Placanje sacuvanoPlacanje=placanjeRepository.save(novoPlacanje);
        return PlacanjeResponse.from(sacuvanoPlacanje);
    }
    /**
     * Retrieves a list of all payment transactions recorded within the system.
     *
     * @return a list of {@link PlacanjeResponse} objects.
     */
    public List<PlacanjeResponse> findAll(){
        log.info("Finding all payments");
        return PlacanjeResponse.from(placanjeRepository.findAll());
    }
    /**
     * Retrieves a specific payment record by its unique identifier.
     *
     * @param id the unique ID of the payment transaction.
     * @return {@link PlacanjeResponse} representing the requested payment entry.
     * @throws RuntimeException if no payment is found with the specified identifier.
     */
    public PlacanjeResponse findById(Long id){
        log.info("Finding payment");
        return PlacanjeResponse.from(findByIdInternal(id));
    }
    /**
     * Internal method to safely look up and fetch a {@link Placanje} entity.
     * Bypasses the DTO mapper mapping step for fast internal component operations.
     *
     * @param id the unique ID of the payment transaction.
     * @return the {@link Placanje} database entity.
     * @throws RuntimeException if no transaction records match the given ID.
     */
    private Placanje findByIdInternal(Long id) {
        Optional<Placanje> placanje=placanjeRepository.findById(id);
        if(placanje.isPresent()){
            return placanje.get();
        }throw new RuntimeException("No payment in system");
    }
}
