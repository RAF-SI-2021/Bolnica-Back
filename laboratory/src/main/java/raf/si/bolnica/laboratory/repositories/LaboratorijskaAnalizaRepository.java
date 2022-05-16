package raf.si.bolnica.laboratory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;


@Repository
public interface LaboratorijskaAnalizaRepository extends JpaRepository<LaboratorijskaAnaliza, Long> {
}
