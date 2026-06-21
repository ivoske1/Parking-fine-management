package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/**
 * Represents a vehicle tracked within the parking management system.
 * Enforces a unique constraint on its license plate number and acts as the central
 * link connecting a driver ({@link Vozac}) to their issued parking citations ({@link Kazna})
 * and active daily parking tickets ({@link DnevnaKarta}).
 * * @author Your Name
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="vozilo")
@Getter
@Setter
public class Vozilo {
    /**
     * Unique identifier for the vehicle in the database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The official license plate registration number of the vehicle (e.g., BG-123-XX).
     * This field enforces a strict unique database constraint to ensure no duplicate vehicles exist.
     */
    @NotNull(message = "Registracija je obavezna")
    @Column(name="registracija",unique = true)
    private String registracija;
    /**
     * The manufacturer or brand of the vehicle (e.g., Fiat, Volkswagen, BMW).
     */
    @NotNull(message = "Marka je obavezna")
    @Column(name="marka",nullable = false)
    private String marka;
    /**
     * The specific commercial model name of the vehicle (e.g., Punto, Golf, 3 Series).
     */
    @NotNull(message = "Model je obavezan")
    @Column(name="model",nullable = false)
    private String model;
    /**
     * The registered driver who owns or is primary responsible for the vehicle.
     * This field cannot be null, establishing a mandatory relationship.
     */
    @NotNull(message = "Vozač je obavezan")
    @ManyToOne
    @JoinColumn(name="vozac_id",nullable = false)
    private Vozac vozac;
    /**
     * The collection of all parking citations (fines) issued to this vehicle.
     * Managed bidirectionally; changes to the vehicle lifecycle cascade directly to these records.
     */
    @OneToMany(mappedBy = "vozilo",cascade=CascadeType.ALL)
    private List<Kazna> kazne;
    /**
     * The historical log of daily parking tickets purchased for this vehicle.
     * Managed bidirectionally; lifecycle events cascade down to the associated ticket instances.
     */
    @OneToMany(mappedBy = "vozilo",cascade=CascadeType.ALL)
    private List<DnevnaKarta>dnevneKarte;


}
