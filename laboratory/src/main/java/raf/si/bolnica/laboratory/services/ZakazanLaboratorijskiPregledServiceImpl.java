package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;
import raf.si.bolnica.laboratory.repositories.ZakazanLaboratorijskiPregledRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class ZakazanLaboratorijskiPregledServiceImpl implements ZakazanLaboratorijskiPregledService {


    @Autowired
    ZakazanLaboratorijskiPregledRepository repository;

    @Override
    public ZakazanLaboratorijskiPregled saveZakazanPregled(ZakazanLaboratorijskiPregled pregled) {
        Optional<ZakazanLaboratorijskiPregled> toSave = repository.
                findZakazanLaboratorijskiPregledByLbzEqualsAndZakazanDatum(pregled.getLbz(), pregled.getZakazanDatum());

        if (toSave.isEmpty()) {
            return repository.save(pregled);
        }

        return null;
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
