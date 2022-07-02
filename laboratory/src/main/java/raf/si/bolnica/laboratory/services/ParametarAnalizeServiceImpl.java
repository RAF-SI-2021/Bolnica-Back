package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.entities.ParametarAnalize;
import raf.si.bolnica.laboratory.repositories.ParametarAnalizeRepository;

import java.util.List;

@Service
public class ParametarAnalizeServiceImpl implements ParametarAnalizeService {

    @Autowired
    ParametarAnalizeRepository parametarAnalizeRepository;

    @Override
    public ParametarAnalize getParametarAnalize(Long id) {
        return parametarAnalizeRepository.getOne(id);
    }

    @Override
    public List<ParametarAnalize> getParametriAnalize() {
        return parametarAnalizeRepository.findAll();
    }

    @Override
    public ParametarAnalize updateParametarAnalize(ParametarAnalize parametarAnalize) {
        return parametarAnalizeRepository.save(parametarAnalize);
    }

    @Override
    public ParametarAnalize saveParametarAnalize(ParametarAnalize parametarAnalize) {
        return parametarAnalizeRepository.save(parametarAnalize);
    }

    @Override
    public void deleteParametarAnalize(Long id) {
        parametarAnalizeRepository.deleteById(id);
    }

    @Override
    public List<ParametarAnalize> getParametarAnalizeByLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza) {
        return parametarAnalizeRepository.findByLaboratorijskaAnaliza(laboratorijskaAnaliza);
    }
}
