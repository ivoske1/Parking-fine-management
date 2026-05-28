package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.parking_kazne.enums.Zona;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="parkingZona")
@Getter
@Setter
public class ParkingZona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="naziv")
    private String naziv;

    @Column(name="cenaPoSatu",nullable = false)
    private Double cenaPoSatu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Zona zona;

}
