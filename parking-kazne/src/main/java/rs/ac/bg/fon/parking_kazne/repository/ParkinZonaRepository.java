package rs.ac.bg.fon.parking_kazne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.parking_kazne.entity.ParkingZona;

import java.util.List;

public interface ParkinZonaRepository extends JpaRepository<ParkingZona,Long> {
}
