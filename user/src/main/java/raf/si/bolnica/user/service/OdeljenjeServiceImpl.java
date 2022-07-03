package raf.si.bolnica.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.repositories.OdeljenjeRepository;
import raf.si.bolnica.user.repositories.ZdravstvenaUstanovaRepository;

import java.util.Iterator;
import java.util.List;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class OdeljenjeServiceImpl implements OdeljenjeService {

    @Autowired
    OdeljenjeRepository odeljenjeRepository;

    @Autowired
    ZdravstvenaUstanovaRepository zdravstvenaUstanovaRepository;

    @Override
    public Odeljenje fetchOdeljenjeById(long id) {
        return odeljenjeRepository.findByOdeljenjeId(id);
    }

    @Override
    public List<Odeljenje> findAll() {
        return odeljenjeRepository.findAll();
    }

    @Override
    public List<Odeljenje> findAllByPbb() {
        //        allDepartments.removeIf(odeljenje -> odeljenje.getBolnica().getPoslovniBrojBolnice() != pbb);

        return findAll();
    }

    @Override
    public List<ZdravstvenaUstanova> findAllHospitals() {
        return zdravstvenaUstanovaRepository.findAll();
    }

    @Override
    @Transactional()
    public Odeljenje saveOdeljenje(Odeljenje odeljenje) {
        return odeljenjeRepository.save(odeljenje);
    }

    @Override
    public List<Odeljenje> searchByNaziv(String naziv) {
        return odeljenjeRepository.findByNazivContaining(naziv);
    }
}
