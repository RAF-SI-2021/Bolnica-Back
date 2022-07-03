package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;
import raf.si.bolnica.laboratory.repositories.LaboratorijskiRadniNalogRepository;

import java.util.List;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class LaboratorijskiRadniNalogServiceImpl implements LaboratorijskiRadniNalogService {

    @Autowired
    LaboratorijskiRadniNalogRepository repository;


    @Override
    public LaboratorijskiRadniNalog getRadniNalog(Long id) {
        return repository.getOne(id);
    }

    @Override
    public LaboratorijskiRadniNalog fetchRadniNalogById(Long id) {
        return repository.findByLaboratorijskiRadniNalogId(id);
    }

    @Override
    public List<LaboratorijskiRadniNalog> getRadniNalozi() {
        return repository.findAll();
    }

    @Override
    @Transactional()
    public LaboratorijskiRadniNalog updateRadniNalog(LaboratorijskiRadniNalog radniNalog) {
        return repository.save(radniNalog);
    }

    @Override
    @Transactional()
    public LaboratorijskiRadniNalog saveRadniNalog(LaboratorijskiRadniNalog radniNalog) {
        System.out.println("Servis " + radniNalog.getLbp());
        return repository.save(radniNalog);
    }

    @Override
    @Transactional()
    public void deleteRadniNalog(Long id) {
        repository.deleteById(id);
    }
}
