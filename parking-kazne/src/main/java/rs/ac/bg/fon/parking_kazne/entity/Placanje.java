package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.bg.fon.parking_kazne.enums.NacinPlacanja;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="placanje")
@Getter
@Setter
public class Placanje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="iznos")
    private double iznos;

    @Column(name="datum")
    private LocalDate datum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NacinPlacanja nacinPlacanja;

    @OneToOne
    @JoinColumn(name="kazna_id",nullable = false,unique = true)
    private Kazna kazna;
}
