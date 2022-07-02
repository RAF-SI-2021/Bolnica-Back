package raf.si.bolnica.management.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.PosetaPacijentu;

import java.util.List;
import java.util.UUID;

@Repository
public interface PosetPacijentuRepository extends JpaRepository<PosetaPacijentu, Long> {

    List<PosetaPacijentu> getPosetaPacijentuByLbpPacijenta(UUID lbp);
}
