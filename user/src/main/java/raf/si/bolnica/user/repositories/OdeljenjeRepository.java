package raf.si.bolnica.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.bolnica.user.models.Odeljenje;

@Repository
public interface OdeljenjeRepository extends JpaRepository<Odeljenje, Long> {}
