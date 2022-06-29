package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.management.entities.ZakazaniTerminPrijema;

import java.util.List;
import java.util.UUID;


public interface ZakazaniTerminPrijemaRepository extends JpaRepository<ZakazaniTerminPrijema, Long> {

    List<ZakazaniTerminPrijema> findAllByOdeljenjeId(long id);

    List<ZakazaniTerminPrijema> findAllByOdeljenjeIdAndLbpPacijentaEquals(long id, UUID lbp);

}
