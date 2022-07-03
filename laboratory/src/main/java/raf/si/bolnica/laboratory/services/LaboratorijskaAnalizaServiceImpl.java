package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.repositories.LaboratorijskaAnalizaRepository;

import java.util.List;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class LaboratorijskaAnalizaServiceImpl implements LaboratorijskaAnalizaService{

    @Autowired
    LaboratorijskaAnalizaRepository laboratorijskaAnalizaRepository;

    @Override
    public LaboratorijskaAnaliza getLaboratorijskaAnaliza(Long id) {
        return laboratorijskaAnalizaRepository.getOne(id);
    }

    @Override
    public List<LaboratorijskaAnaliza> getLaboratorijskeAnalize() {
        return laboratorijskaAnalizaRepository.findAll();
    }

    @Override
    @Transactional()
    public LaboratorijskaAnaliza updateLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza) {
        return laboratorijskaAnalizaRepository.save(laboratorijskaAnaliza);
    }

    @Override
    @Transactional()
    public LaboratorijskaAnaliza saveLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza) {
        return laboratorijskaAnalizaRepository.save(laboratorijskaAnaliza);
    }

    @Override
    @Transactional()
    public void deleteLaboratorijskaAnaliza(Long id) {
        laboratorijskaAnalizaRepository.deleteById(id);
    }

    @Override
    public List<LaboratorijskaAnaliza> getLaboratorijskaAnalizaBySkracenica(String skracenica) {
        return laboratorijskaAnalizaRepository.findBySkracenica(skracenica);
    }
}
