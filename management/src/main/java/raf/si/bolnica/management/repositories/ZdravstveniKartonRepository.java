package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.si.bolnica.management.entities.ZdravstveniKarton;

public interface ZdravstveniKartonRepository extends JpaRepository<ZdravstveniKarton, Long> {
}
