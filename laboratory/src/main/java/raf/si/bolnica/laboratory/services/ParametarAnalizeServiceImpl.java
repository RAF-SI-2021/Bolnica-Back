package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.entities.ParametarAnalize;
import raf.si.bolnica.laboratory.repositories.ParametarAnalizeRepository;

import java.util.List;

@Service
@Transactional(value = "transactionManager", readOnly = true)
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
    @Transactional()
    public ParametarAnalize updateParametarAnalize(ParametarAnalize parametarAnalize) {
        return parametarAnalizeRepository.save(parametarAnalize);
    }

    @Override
    @Transactional()
    public ParametarAnalize saveParametarAnalize(ParametarAnalize parametarAnalize) {
        return parametarAnalizeRepository.save(parametarAnalize);
    }

    @Override
    @Transactional()
    public void deleteParametarAnalize(Long id) {
        parametarAnalizeRepository.deleteById(id);
    }

    @Override
    public List<ParametarAnalize> getParametarAnalizeByLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza) {
        return parametarAnalizeRepository.findByLaboratorijskaAnaliza(laboratorijskaAnaliza);
    }
}
