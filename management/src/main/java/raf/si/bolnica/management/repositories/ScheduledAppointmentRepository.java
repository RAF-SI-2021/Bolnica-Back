package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.ZakazaniPregled;


@Repository
public interface ScheduledAppointmentRepository extends JpaRepository<ZakazaniPregled, Long> {
            ZakazaniPregled getZakazaniPregledByZakazaniPregledId(long id);
}
