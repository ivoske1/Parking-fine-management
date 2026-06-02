package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dnevna_karta")
@Getter
@Setter
public class DnevnaKarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private int brojSati;

    @Column(nullable = true)
    private double cena;

    @Column(nullable = true)
    private LocalDateTime vremeKupovine;

    @Column(nullable = true)
    private LocalDateTime vaziDo;

    @ManyToOne
    @JoinColumn(name="vozilo_id",nullable = true)
    private Vozilo vozilo;

    @ManyToOne
    @JoinColumn(name="parkingZona_id",nullable = true)
    private ParkingZona parkingZona;

}
