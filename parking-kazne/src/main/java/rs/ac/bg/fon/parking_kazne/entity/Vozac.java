package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
/**
 * Represents a registered vehicle driver within the parking system database.
 * Holds individual identification details, driving license data, contact information,
 * and tracks the collection of all vehicles registered under the driver's profile.
 * * @author Lazar Ivosevic
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="vozac")
@Getter
@Setter
public class Vozac {
    /**
     * Unique identifier for the driver in the database (Primary Key).
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * The first name of the driver.
     */
    @Column(nullable = true)
    private String ime;
    /**
     * The last name (surname) of the driver.
     */
    @Column(nullable = true)
    private String prezime;
    /**
     * The official serial number of the driver's driver's license.
     */
    @Column(nullable = true)
    private String brojVozacke;
    /**
     * The contact phone number of the driver, used for automated SMS parking warnings or receipts.
     */
    @Column(nullable = true)
    private String telefon;
    /**
     * The list of vehicles owned by or registered to this specific driver.
     * Managed bidirectionally; state alterations on the driver instance cascade directly
     * to all associated vehicle records.
     */
    @OneToMany(mappedBy = "vozac",cascade = CascadeType.ALL)
    private List<Vozilo>vozila;
}
