package rs.ac.bg.fon.parking_kazne.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="lokacija")
@Getter
@Setter
@NoArgsConstructor
public class Lokacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ulica",nullable = true)
    private String ulica;

    @Column(name="grad",nullable = true)
    private String grad;
}
