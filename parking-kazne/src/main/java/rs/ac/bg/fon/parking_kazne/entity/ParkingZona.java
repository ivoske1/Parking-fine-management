package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.parking_kazne.enums.Zona;
/**
 * Represents a designated parking zone within the municipality's parking system.
 * Configures the pricing policies applied per hour and maps the configuration
 * to a specific classification category via {@link Zona}.
 * * @author Lazar Ivosevic
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="parking_zona")
@Getter
@Setter
public class ParkingZona {
    /**
     * Unique identifier for the parking zone record in the database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The descriptive name of the parking zone (e.g., "A1 - Center", "Zone 2 - Public").
     */
    @Column(name="naziv")
    private String naziv;
    /**
     * The hourly parking rate charged for keeping a vehicle within this zone.
     * This field cannot be null and serves as a base multiplier for ticket creation.
     */
    @Column(name="cenaPoSatu",nullable = false)
    private Double cenaPoSatu;
    /**
     * The structural category enum representing the type of zone (e.g., RED, YELLOW, GREEN).
     * This field cannot be null and enforces domain-level classification constraints.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Zona zona;

}
