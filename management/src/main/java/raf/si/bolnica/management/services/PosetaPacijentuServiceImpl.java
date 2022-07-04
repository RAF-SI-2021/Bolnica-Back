package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.PosetaPacijentu;
import raf.si.bolnica.management.repositories.PosetPacijentuRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class PosetaPacijentuServiceImpl implements PosetaPacijentuService {

    @Autowired
    PosetPacijentuRepository posetPacijentuRepository;

    @Override
    @CachePut(value = "visits", key = "#posetaPacijentu.lbpPacijenta")
    @Transactional()
    public PosetaPacijentu save(PosetaPacijentu posetaPacijentu) {
        return posetPacijentuRepository.save(posetaPacijentu);
    }

    @Override
//    @Cacheable(value = "visits", key = "#lbp")
    public List<PosetaPacijentu> findAllByLBP(UUID lbp) {
        return posetPacijentuRepository.getPosetaPacijentuByLbpPacijenta(lbp);
    }

}
