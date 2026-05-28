package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="vozac")
@Getter
@Setter
public class Vozac {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    @Column(nullable = false)
    private String brojVozacke;

    @Column(nullable = false)
    private String telefon;

    @OneToMany(mappedBy = "vozac",cascade = CascadeType.ALL)
    private List<Vozilo>vozila;
}
