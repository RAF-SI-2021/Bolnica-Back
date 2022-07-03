package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalizeKey;
import raf.si.bolnica.laboratory.entities.*;
import raf.si.bolnica.laboratory.repositories.ParametarAnalizeRepository;
import raf.si.bolnica.laboratory.repositories.RezultatParametraAnalizeRepository;

import java.util.List;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class RezultatParametraAnalizeServiceImpl implements RezultatParametraAnalizeService {

    @Autowired
    RezultatParametraAnalizeRepository rezultatParametraAnalizeRepository;

    @Override
    public RezultatParametraAnalize getRezultatParametraAnalize(RezultatParametraAnalizeKey id) {
        List<RezultatParametraAnalize> rezultati =  rezultatParametraAnalizeRepository.findById(id);
        if(rezultati.isEmpty()) return null;
        else return rezultati.get(0);
    }

    @Override
    public List<RezultatParametraAnalize> getRezultateParametaraAnalize() {
        return rezultatParametraAnalizeRepository.findAll();
    }

    @Override
    public List<RezultatParametraAnalize> getRezultateParametaraAnalizeByRadniNalog(LaboratorijskiRadniNalog labRadniNalog) {
        return rezultatParametraAnalizeRepository.findByLaboratorijskiRadniNalog(labRadniNalog);
    }

    @Override
    @Transactional()
    public RezultatParametraAnalize updateRezultatParametraAnalize(RezultatParametraAnalize rezultatParametraAnalize) {
        return rezultatParametraAnalizeRepository.save(rezultatParametraAnalize);
    }

    @Override
    @Transactional()
    public RezultatParametraAnalize saveRezultatParametraAnalize(RezultatParametraAnalize rezultatParametraAnalize) {
        return rezultatParametraAnalizeRepository.save(rezultatParametraAnalize);
    }

    @Override
    public void deleteRezultatParametraAnalize(Long id) {
        rezultatParametraAnalizeRepository.deleteById(id);
    }

}
