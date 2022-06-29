package raf.si.bolnica.management.repositories;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.StanjePacijenta;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StanjePacijentaRepository extends JpaRepository<StanjePacijenta, Long>{

}
