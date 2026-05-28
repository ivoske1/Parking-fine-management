package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dnevnaKarta")
@Getter
@Setter
public class DnevnaKarta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int brojSati;

    @Column(nullable = false)
    private double cena;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(nullable = false)
    private LocalDateTime vaziDo;

    @ManyToOne
    @JoinColumn(name="vozilo_id",nullable = false)
    private Vozilo vozilo;

    @ManyToOne
    @JoinColumn(name="parkingZona_id",nullable = false)
    private ParkingZona parkingZona;

}
