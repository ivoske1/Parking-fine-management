package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Represents a parking citation (fine) issued to a vehicle due to a parking violation.
 * Tracks the issuance details, the financial amount, the current processing status,
 * the controller who issued it, and its geographic and zonal context.
 *
 * @author Lazar Ivosevic
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="kazna")
@Getter
@Setter
public class Kazna {
    /**
     * Unique identifier for the parking citation in the database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The exact date and time when the parking citation was issued.
     */
    @Column(name="datum")
    private LocalDateTime vremeIzdavanja;
    /**
     * The total monetary fine amount charged for the violation.
     */
    @Column(name="iznos")
    private double iznos;
    /**
     * The current lifecycle status of the citation (e.g., UNPAID, PAID, OVERDUE).
     */
    @Enumerated(EnumType.STRING)
    private StatusKazne statusKazne;
    /**
     * The specific vehicle that committed the violation and received the citation.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name="vozilo_id",nullable = false)
    private Vozilo vozilo;
    /**
     * The parking controller (officer) who identified the violation and issued this citation.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name="kontrolor_id",nullable = false)
    private Kontrolor kontrolor;
    /**
     * The precise geographical location or street address where the violation occurred.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name="lokacija_id",nullable = false)
    private Lokacija lokacija;
    /**
     * The designated parking zone in which the vehicle was illegally parked.
     * This field cannot be null.
     */
    @ManyToOne
    @JoinColumn(name="parkingZona_id",nullable = false)
    private ParkingZona parkingZona;
    /**
     * The payment transaction record associated with this citation, if any.
     * Managed bidirectionally; changes cascade to the payment record.
     */
    @OneToOne(mappedBy = "kazna",cascade = CascadeType.ALL)
    private Placanje placanje;
}
