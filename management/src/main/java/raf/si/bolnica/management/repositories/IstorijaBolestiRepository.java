package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.IstorijaBolesti;
import raf.si.bolnica.management.entities.ZdravstveniKarton;

@Repository
public interface IstorijaBolestiRepository extends JpaRepository<IstorijaBolesti, Long> {
    IstorijaBolesti getIstorijaBolestiByZdravstveniKartonAndPodaciValidni(ZdravstveniKarton zdravstveniKarton, Boolean validni);
}
