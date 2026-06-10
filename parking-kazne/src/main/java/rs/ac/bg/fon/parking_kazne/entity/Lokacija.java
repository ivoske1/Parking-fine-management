package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Represents a geographical location or address within the parking system.
 * Used to pinpoint exactly where a parking violation took place by storing
 * the city and street name.
 * * @author Lazar Ivosevic
 */
@Entity
@Table(name="lokacija")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lokacija {
    /**
     * Unique identifier for the location record in the database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The name of the street where the location is situated.
     */
    @Column(name="ulica",nullable = true)
    private String ulica;
    /**
     * The name of the city where the location is situated (e.g., Beograd).
     */
    @Column(name="grad",nullable = true)
    private String grad;
}
