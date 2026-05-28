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
@Table(name="kontrolor")
@Getter
@Setter
public class Kontrolor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ime")
    private String ime;

    @Column(name="prezime")
    private String prezime;

    @Column(name="brojLegitimacije",unique = true)
    private String brojLegitimacije;
    @OneToMany(mappedBy = "kontrolor",cascade = CascadeType.ALL)
    private List<Kazna>kazne;

}
