package rs.ac.bg.fon.parking_kazne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.parking_kazne.entity.Vozilo;

public interface VoziloRepository extends JpaRepository<Vozilo,Long> {
}
