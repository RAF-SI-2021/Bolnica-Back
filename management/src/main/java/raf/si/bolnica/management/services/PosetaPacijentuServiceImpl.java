package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.BolnickaSoba;
import raf.si.bolnica.management.entities.PosetaPacijentu;
import raf.si.bolnica.management.repositories.BolnickaSobaRepository;
import raf.si.bolnica.management.repositories.PosetPacijentuRepository;
import raf.si.bolnica.management.services.bolnickaSoba.BolnickaSobaService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional("transactionManager")
public class PosetaPacijentuServiceImpl implements PosetaPacijentuService {

    @Autowired
    PosetPacijentuRepository posetPacijentuRepository;

    @Override
    public PosetaPacijentu save(PosetaPacijentu posetaPacijentu) {
        return posetPacijentuRepository.save(posetaPacijentu);
    }

    @Override
    public List<PosetaPacijentu> findAllByLBP(UUID lbp) {
        return posetPacijentuRepository.getPosetaPacijentuByLbpPacijenta(lbp);
    }

}
