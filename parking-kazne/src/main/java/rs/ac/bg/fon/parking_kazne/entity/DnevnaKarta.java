package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * Represents a daily parking ticket issued for a specific vehicle within a designated parking zone.
 * It contains information regarding the ticket pricing, purchase timestamp, and its expiration period.
 *
 * @author Lazar Ivosevic
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dnevna_karta")
@Getter
@Setter
public class DnevnaKarta {
    /**
     * Unique identifier for the daily ticket in the database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The number of hours for which the daily ticket remains valid.
     */
    @Column(nullable = true)
    private int brojSati;
    /**
     * The price of the daily ticket, typically expressed in RSD.
     */
    @Column(nullable = true)
    private double cena;
    /**
     * The exact date and time when the daily ticket was purchased.
     */
    @Column(nullable = true)
    private LocalDateTime vremeKupovine;
    /**
     * The expiration date and time indicating until when the daily ticket is valid.
     */
    @Column(nullable = true)
    private LocalDateTime vaziDo;
    /**
     * The vehicle associated with this daily ticket.
     * Multiple daily tickets can be associated with a single vehicle over time.
     */
    @ManyToOne
    @JoinColumn(name="vozilo_id",nullable = true)
    private Vozilo vozilo;
    /**
     * The specific parking zone where this daily ticket is applicable (e.g., Red, Yellow, Green zone).
     */
    @ManyToOne
    @JoinColumn(name="parkingZona_id",nullable = true)
    private ParkingZona parkingZona;

}
