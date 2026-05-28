package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="vozilo")
@Getter
@Setter
public class Vozilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="registracija",unique = true)
    private String registracija;

    @Column(name="marka")
    private String marka;

    @Column(name="model")
    private String model;

    @ManyToOne
    @JoinColumn(name="vozac_id",nullable = false)
    private Vozac vozac;

    @OneToMany(mappedBy = "vozilo",cascade=CascadeType.ALL)
    private List<Kazna> kazne;

    @OneToMany(mappedBy = "vozilo",cascade=CascadeType.ALL)
    private List<DnevnaKarta>dnevneKarte;


}
