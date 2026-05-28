package rs.ac.bg.fon.parking_kazne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.parking_kazne.entity.Lokacija;

@Repository
public interface LokacijaRepository extends JpaRepository<Lokacija,Long> {

}
