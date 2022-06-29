package raf.si.bolnica.management.services;

import raf.si.bolnica.management.entities.StanjePacijenta;
import raf.si.bolnica.management.repositories.StanjePacijentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional("transactionManager")
public class StanjePacijentaServiceImpl implements StanjePacijentaService {


    @Autowired
    StanjePacijentaRepository stanjePacijentaRepository;

    @Override
    public StanjePacijenta saveStanje(StanjePacijenta stanje) {
        return  stanjePacijentaRepository.save(stanje);
    }
}
