package raf.si.bolnica.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.management.entities.Alergen;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlergenRepository extends JpaRepository<Alergen, Long> {

    @Override
    Optional<Alergen> findById(Long aLong);

    @Override
    List<Alergen> findAll();


}
