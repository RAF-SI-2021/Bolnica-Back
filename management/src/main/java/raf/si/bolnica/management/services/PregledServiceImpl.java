package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.repositories.IstorijaBolestiRepository;
import raf.si.bolnica.management.repositories.PacijentRepository;
import raf.si.bolnica.management.repositories.PregledRepository;
import raf.si.bolnica.management.repositories.ZdravstveniKartonRepository;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class PregledServiceImpl implements PregledService {

    @Autowired
    PregledRepository pregledRepository;

    @Autowired
    private IstorijaBolestiRepository istorijaBolestiRepository;

    @Autowired
    private ZdravstveniKartonRepository zdravstveniKartonRepository;

    @Autowired
    private PacijentRepository pacijentRepository;


    @Override
    @Transactional()
    public Pregled savePregled(Pregled pregled) {
        return pregledRepository.save(pregled);
    }

}
