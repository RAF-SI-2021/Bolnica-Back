package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.repositories.PacijentRepository;

import java.util.UUID;

@Service
@Transactional("transactionManager")
public class PacijentServiceImpl implements  PacijentService{

    @Autowired
    private PacijentRepository pacijentRepository;

    @Override
    public Pacijent fetchPacijentByLBP(UUID lbp) {
        return pacijentRepository.findByLBP(lbp);
    }

    @Override
    public Pacijent fetchPacijentById(Long id) {
        return pacijentRepository.findByPacijentId(id);
    }

    @Override
    public Pacijent savePacijent(Pacijent pacijent) {
        return pacijentRepository.save(pacijent);
    }

    @Override
    public void deleteById(Long id) {
        pacijentRepository.deleteById(id);
    }
}
