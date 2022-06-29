package raf.si.bolnica.laboratory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;

import java.util.List;

@Repository
public interface LaboratorijskaAnalizaRepository extends JpaRepository<LaboratorijskaAnaliza, Long> {

    List<LaboratorijskaAnaliza> findBySkracenica(String skracenica);
}
