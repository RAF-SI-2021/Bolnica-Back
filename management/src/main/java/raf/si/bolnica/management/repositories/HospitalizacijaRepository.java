package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.Hospitalizacija;

import java.util.List;
import java.util.UUID;

@Repository
public interface HospitalizacijaRepository extends JpaRepository<Hospitalizacija, Long> {
    List<Hospitalizacija> findAllByLbpPacijenta(UUID lbp);
}
