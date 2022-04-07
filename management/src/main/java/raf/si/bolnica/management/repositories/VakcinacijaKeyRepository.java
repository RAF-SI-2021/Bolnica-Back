package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.management.entities.AlergenZdravstveniKarton;
import raf.si.bolnica.management.entities.VakcinacijaKey;

public interface VakcinacijaKeyRepository extends JpaRepository<VakcinacijaKey, Long>  {
}
