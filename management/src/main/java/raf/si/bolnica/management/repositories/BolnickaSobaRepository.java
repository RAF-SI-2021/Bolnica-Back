package raf.si.bolnica.management.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.BolnickaSoba;

@Repository
public interface BolnickaSobaRepository extends JpaRepository<BolnickaSoba, Long> {
    BolnickaSoba getByBolnickaSobaId(long bolnickaSobaId);
}
