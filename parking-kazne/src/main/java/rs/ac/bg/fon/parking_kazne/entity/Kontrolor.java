package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
/**
 * Represents a parking controller (officer) employed by the parking service.
 * Enforces a uniqueness constraint on the controller's official badge number
 * and maintains a record of all citations issued by them.
 *
 * @author Lazar Ivosevic
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="kontrolor")
@Getter
@Setter
public class Kontrolor {
    /**
     * Unique identifier for the parking controller in the database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The first name of the parking controller.
     */
    @NotNull(message = "Ime je obavezno")
    @Column(name="ime",nullable = false)
    private String ime;
    /**
     * The last name (surname) of the parking controller.
     */
    @NotNull(message = "prezime je obavezno")
    @Column(name="prezime",nullable = false)
    private String prezime;
    /**
     * The official, unique badge or identification card number assigned to the controller.
     * This field enforces a unique database constraint to avoid duplicate officer profiles.
     */
    @NotNull(message = "Broj legitimacije je obavezan")
    @Column(name="brojLegitimacije",unique = true)
    private String brojLegitimacije;
    /**
     * The list of all parking citations (fines) issued by this specific controller over time.
     * Managed bidirectionally; operations on the controller cascade to their associated citations.
     */
    @OneToMany(mappedBy = "kontrolor",cascade = CascadeType.ALL)
    private List<Kazna>kazne;

}
