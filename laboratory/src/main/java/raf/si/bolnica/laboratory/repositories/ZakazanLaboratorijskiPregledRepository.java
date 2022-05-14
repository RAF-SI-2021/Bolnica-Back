package raf.si.bolnica.laboratory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;

import java.sql.Date;
import java.util.List;

@Repository
public interface ZakazanLaboratorijskiPregledRepository extends JpaRepository<ZakazanLaboratorijskiPregled, Long> {
    List<ZakazanLaboratorijskiPregled> findByZakazanDatum(Date zakazanDatum);
}
