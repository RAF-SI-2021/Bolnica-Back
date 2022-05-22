package raf.si.bolnica.laboratory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.laboratory.entities.Parametar;


@Repository
public interface ParametarRepository extends JpaRepository<Parametar, Long> {

}
