package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.LekarskiIzvestajStacionar;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface LekarskiIzvestajRepository extends JpaRepository<LekarskiIzvestajStacionar, Long> {

    List<LekarskiIzvestajStacionar> findAllByLbpPacijenta(UUID lbpPacijenta);

    List<LekarskiIzvestajStacionar> findAllByLbpPacijentaAndDatumVremeKreiranja(UUID lbpPacijenta, Date datumVremeKreiranja);

    List<LekarskiIzvestajStacionar> findAllByLbpPacijentaAndDatumVremeKreiranjaGreaterThanEqualAndDatumVremeKreiranjaLessThanEqual(UUID lbpPacijenta, Date datumVremeKreiranja, Date datumVremeKreiranja2);
}
