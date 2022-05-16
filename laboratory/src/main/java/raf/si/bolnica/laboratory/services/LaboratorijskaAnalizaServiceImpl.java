package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.repositories.LaboratorijskaAnalizaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
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
    public LaboratorijskaAnaliza updateLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza) {
        return laboratorijskaAnalizaRepository.save(laboratorijskaAnaliza);
    }

    @Override
    public LaboratorijskaAnaliza saveLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza) {
        return laboratorijskaAnalizaRepository.save(laboratorijskaAnaliza);
    }

    @Override
    public void deleteLaboratorijskaAnaliza(Long id) {
        laboratorijskaAnalizaRepository.deleteById(id);
    }

    @Override
    public List<LaboratorijskaAnaliza> getLaboratorijskaAnalizaBySkracenica(String skracenica) {
        return laboratorijskaAnalizaRepository.findBySkracenica(skracenica);
    }
}
