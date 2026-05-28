package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="kazna")
@Getter
@Setter
public class Kazna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="datum")
    private LocalDate datum;

    @Column(name="iznos")
    private double iznos;

    @Enumerated(EnumType.STRING)
    private StatusKazne statusKazne;

    @ManyToOne
    @JoinColumn(name="vozilo_id",nullable = false)
    private Vozilo vozilo;

    @ManyToOne
    @JoinColumn(name="kontrolor_id",nullable = false)
    private Kontrolor kontrolor;

    @ManyToOne
    @JoinColumn(name="lokacija_id",nullable = false)
    private Lokacija lokacija;

    @ManyToOne
    @JoinColumn(name="parkingZona_id",nullable = false)
    private ParkingZona parkingZona;

    @OneToOne(mappedBy = "kazna",cascade = CascadeType.ALL)
    private Placanje placanje;
}
