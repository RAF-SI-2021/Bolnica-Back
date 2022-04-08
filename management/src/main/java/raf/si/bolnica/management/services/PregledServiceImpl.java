package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.repositories.PregledRepository;

@Service
@Transactional("transactionManager")
public class PregledServiceImpl implements PregledService{

    @Autowired
    PregledRepository pregledRepository;

    @Override
    public Pregled savePregled(Pregled pregled) {
        return pregledRepository.save(pregled);
    }
}
