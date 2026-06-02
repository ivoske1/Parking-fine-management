package rs.ac.bg.fon.parking_kazne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.parking_kazne.entity.Kazna;
import rs.ac.bg.fon.parking_kazne.enums.StatusKazne;

import java.util.List;

public interface KaznaRepository extends JpaRepository <Kazna,Long> {
}
