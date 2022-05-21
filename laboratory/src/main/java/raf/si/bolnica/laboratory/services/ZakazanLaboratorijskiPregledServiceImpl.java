package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;
import raf.si.bolnica.laboratory.repositories.ZakazanLaboratorijskiPregledRepository;

import java.util.List;

@Service
public class ZakazanLaboratorijskiPregledServiceImpl implements ZakazanLaboratorijskiPregledService {


    @Autowired
    ZakazanLaboratorijskiPregledRepository repository;

    @Override
    public ZakazanLaboratorijskiPregled saveZakazanPregled(ZakazanLaboratorijskiPregled pregled) {
        return repository.save(pregled);
    }

    @Override
    public ZakazanLaboratorijskiPregled getZakazanPregled(Long id) {
        return repository.getOne(id);
    }

    @Override
    public List<ZakazanLaboratorijskiPregled> getZakazaniPregledi() {
        return repository.findAll();
    }

    @Override
    public void deleteZakazanPregled(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ZakazanLaboratorijskiPregled updateZakazanPregled(ZakazanLaboratorijskiPregled pregled) {
        return repository.save(pregled);
    }
}
