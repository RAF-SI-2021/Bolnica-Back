package raf.si.bolnica.laboratory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ZakazanLaboratorijskiPregledRepository extends JpaRepository<ZakazanLaboratorijskiPregled, Long> {
    ZakazanLaboratorijskiPregled findByZakazanLaboratorijskiPregledId(Long id);
    List<ZakazanLaboratorijskiPregled> findByZakazanDatum(Date zakazanDatum);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeId(Integer odeljenjeId);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndZakazanDatum(Integer odeljenjeId, Date zakazanDatum);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndLbp(Integer odeljenjeId, UUID lbp);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndZakazanDatumAndLbp(Integer odeljenjeId,Date zakazanDatum, UUID lbp);
    Optional<ZakazanLaboratorijskiPregled> findZakazanLaboratorijskiPregledByLbzEqualsAndZakazanDatum(UUID lbz, Date datum);
}
