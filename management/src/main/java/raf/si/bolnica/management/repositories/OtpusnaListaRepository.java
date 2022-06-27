package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.OtpusnaLista;

@Repository
public interface OtpusnaListaRepository extends JpaRepository<OtpusnaLista, Long> {

}
