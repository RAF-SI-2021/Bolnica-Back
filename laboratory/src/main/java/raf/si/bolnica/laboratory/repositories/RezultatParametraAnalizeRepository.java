package raf.si.bolnica.laboratory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalizeKey;

import java.util.List;

public interface RezultatParametraAnalizeRepository extends JpaRepository<RezultatParametraAnalize, Long> {
    List<RezultatParametraAnalize> findById(RezultatParametraAnalizeKey id);
}
