package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.laboratory.entities.Parametar;
import raf.si.bolnica.laboratory.repositories.ParametarRepository;

import java.util.List;

@Service
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
    public Parametar updateParametar(Parametar parametar) {
        return parametarRepository.save(parametar);
    }

    @Override
    public Parametar saveParametar(Parametar parametar) {
        return parametarRepository.save(parametar);
    }

    @Override
    public void deleteParametar(Long id) {
        parametarRepository.deleteById(id);
    }
}
