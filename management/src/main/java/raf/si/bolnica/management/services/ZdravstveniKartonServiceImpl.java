package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.repositories.ZdravstveniKartonRepository;



@Service
@Transactional("transactionManager")
public class ZdravstveniKartonServiceImpl implements  ZdravstveniKartonService {

    @Autowired
    ZdravstveniKartonRepository zdravstveniKartonRepository;

    @Override
    public ZdravstveniKarton createZdravstveniKarton(ZdravstveniKarton zdravstveniKarton) {
        return zdravstveniKartonRepository.save(zdravstveniKarton);
    }
}
