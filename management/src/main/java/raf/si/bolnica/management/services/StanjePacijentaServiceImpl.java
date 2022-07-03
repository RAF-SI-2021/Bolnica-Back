package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.StanjePacijenta;
import raf.si.bolnica.management.repositories.StanjePacijentaRepository;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class StanjePacijentaServiceImpl implements StanjePacijentaService {


    @Autowired
    StanjePacijentaRepository stanjePacijentaRepository;

    @Override
    @Transactional()
    public StanjePacijenta saveStanje(StanjePacijenta stanje) {
        return stanjePacijentaRepository.save(stanje);
    }
}
