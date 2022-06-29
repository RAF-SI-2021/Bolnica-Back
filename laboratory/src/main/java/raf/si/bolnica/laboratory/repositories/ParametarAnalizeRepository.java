package raf.si.bolnica.laboratory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.entities.ParametarAnalize;

import java.util.List;

public interface ParametarAnalizeRepository extends JpaRepository<ParametarAnalize, Long> {
    List<ParametarAnalize> findByLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza);
}
