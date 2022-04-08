package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.management.entities.Pregled;

public interface PregledRepository extends JpaRepository<Pregled, Long> {
}
