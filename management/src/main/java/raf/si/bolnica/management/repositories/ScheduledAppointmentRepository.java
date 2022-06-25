package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.ZakazaniPregled;
import raf.si.bolnica.management.entities.enums.StatusPregleda;

import javax.persistence.LockModeType;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ScheduledAppointmentRepository extends JpaRepository<ZakazaniPregled, Long> {
    ZakazaniPregled getZakazaniPregledByZakazaniPregledId(long id);

    List<ZakazaniPregled> findByLbzLekaraAndAndDatumIVremePregleda(UUID lbzLekara, Timestamp datumIVremePregleda);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ZakazaniPregled> findByLbzLekaraAndDatumIVremePregledaBetween(UUID lbzLekara, Timestamp datumStart, Timestamp datumEnd);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ZakazaniPregled> findByLbzLekaraAndDatumIVremePregledaBetweenAndStatusPregleda(UUID lbzLekara, Timestamp datumStart, Timestamp datumEnd, StatusPregleda statusPregleda);

    List<ZakazaniPregled> findByLbzLekara(UUID lbzLekara);

}
