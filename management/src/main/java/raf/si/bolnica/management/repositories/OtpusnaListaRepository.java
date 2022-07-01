package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.OtpusnaLista;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface OtpusnaListaRepository extends JpaRepository<OtpusnaLista, Long> {
    List<OtpusnaLista> findAllByLbpPacijenta(UUID lpbPacijenta);

    List<OtpusnaLista> findAllByLbpPacijentaAndDatumVremeKreiranja(UUID lbpPacijenta, Date datumVremeKreiranja);

    List<OtpusnaLista> findAllByLbpPacijentaAndDatumVremeKreiranjaGreaterThanEqualAndDatumVremeKreiranjaLessThanEqual(UUID lbpPacijenta, Date start, Date end);
}
