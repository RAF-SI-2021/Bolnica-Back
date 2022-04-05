package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.ZdravstveniKarton;

@Repository
public interface ZdravstveniKartonRepository extends JpaRepository<ZdravstveniKarton, Long> {

    ZdravstveniKarton findZdravstveniKartonByPacijent(Pacijent pacijent);
}
