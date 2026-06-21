package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.parking_kazne.enums.NacinPlacanja;

import java.time.LocalDate;
/**
 * Represents a payment transaction record processed for an outstanding parking citation.
 * Enforces a strict one-to-one mapping with a specific citation, ensuring that
 * a single parking fine can only be resolved by exactly one completed payment transaction.
 * * @author Your Name
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="placanje")
@Getter
@Setter
public class Placanje {
    /**
     * Unique identifier for the payment transaction in the database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The exact monetary amount processed and paid by the offender.
     */
    @Positive(message = "Iznos plaćanja mora biti veći od nule")
    @Column(name="iznos",nullable = false)
    private double iznos;
    /**
     * The calendar date when the payment transaction occurred.
     */
    @NotNull(message = "Datum plaćanja je obavezan")
    @Column(name="datum")
    private LocalDate datum;
    /**
     * The medium or channel through which the transaction was settled (e.g., CARD, CASH, SMS, APP).
     * This field cannot be null and relies on structural mapping via {@link NacinPlacanja}.
     */
    @NotNull(message = "Način plaćanja je obavezan")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NacinPlacanja nacinPlacanja;
    /**
     * The specific parking citation that this payment clears out.
     * This field cannot be null and enforces a database-level uniqueness constraint
     * to prevent multiple payment logs from referencing the same citation.
     */
    @NotNull(message = "Kazna je obavezna")
    @OneToOne
    @JoinColumn(name="kazna_id",nullable = false,unique = true)
    private Kazna kazna;

}
