package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.Parametar;
import raf.si.bolnica.laboratory.repositories.ParametarRepository;

import java.util.List;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class ParametarServiceImpl implements ParametarService {

    @Autowired
    ParametarRepository parametarRepository;

    @Override
    public Parametar getParametar(Long id) {
        return parametarRepository.getOne(id);
    }

    @Override
    public List<Parametar> getParametri() {
        return parametarRepository.findAll();
    }

    @Override
    @Transactional()
    public Parametar updateParametar(Parametar parametar) {
        return parametarRepository.save(parametar);
    }

    @Override
    @Transactional()
    public Parametar saveParametar(Parametar parametar) {
        return parametarRepository.save(parametar);
    }

    @Override
    @Transactional()
    public void deleteParametar(Long id) {
        parametarRepository.deleteById(id);
    }
}
