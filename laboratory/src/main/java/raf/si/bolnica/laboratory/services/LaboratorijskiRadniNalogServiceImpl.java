package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;
import raf.si.bolnica.laboratory.repositories.LaboratorijskiRadniNalogRepository;

import java.util.List;

public class LaboratorijskiRadniNalogServiceImpl implements LaboratorijskiRadniNalogService {

    @Autowired
    LaboratorijskiRadniNalogRepository repository;


    @Override
    public LaboratorijskiRadniNalog getRadniNalog(Long id) {
        return repository.getOne(id);
    }

    @Override
    public List<LaboratorijskiRadniNalog> getRadniNalozi() {
        return repository.findAll();
    }

    @Override
    public LaboratorijskiRadniNalog updateRadniNalog(LaboratorijskiRadniNalog radniNalog) {
        return repository.save(radniNalog);
    }

    @Override
    public LaboratorijskiRadniNalog saveRadniNalog(LaboratorijskiRadniNalog radniNalog) {
        return repository.save(radniNalog);
    }

    @Override
    public void deleteRadniNalog(Long id) {
        repository.deleteById(id);
    }
}
