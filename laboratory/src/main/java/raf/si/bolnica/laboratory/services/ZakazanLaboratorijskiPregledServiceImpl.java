package raf.si.bolnica.laboratory.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;
import raf.si.bolnica.laboratory.repositories.ZakazanLaboratorijskiPregledRepository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class ZakazanLaboratorijskiPregledServiceImpl implements ZakazanLaboratorijskiPregledService {


    @Autowired
    ZakazanLaboratorijskiPregledRepository repository;

    @Override
    @Transactional()
    public ZakazanLaboratorijskiPregled saveZakazanPregled(ZakazanLaboratorijskiPregled pregled) {
        return repository.save(pregled);
    }

    @Override
    public ZakazanLaboratorijskiPregled getZakazanPregled(Long id) {
        return repository.findByZakazanLaboratorijskiPregledId(id);
    }

    @Override
    public List<ZakazanLaboratorijskiPregled> getZakazaniPregledi() {
        return repository.findAll();
    }

    @Override
    @Transactional()
    public void deleteZakazanPregled(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional()
    public ZakazanLaboratorijskiPregled updateZakazanPregled(ZakazanLaboratorijskiPregled pregled) {
        return repository.save(pregled);
    }

    @Override
    public List<ZakazanLaboratorijskiPregled> getZakazaniPreglediByDate(Date date) {
        return repository.findByZakazanDatum(date);
    }

    @Override
    public List<ZakazanLaboratorijskiPregled> findByOdeljenjeId(Integer odeljenjeId) {
        return repository.findByOdeljenjeId(odeljenjeId);
    }

    @Override
    public List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndZakazanDatum(Integer odeljenjeId, Date zakazanDatum) {
        return repository.findByOdeljenjeIdAndZakazanDatum(odeljenjeId, zakazanDatum);
    }

    @Override
    public List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndLbp(Integer odeljenjeId, UUID lbp) {
        return repository.findByOdeljenjeIdAndLbp(odeljenjeId, lbp);
    }

    @Override
    public List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndZakazanDatumAndLbp(Integer odeljenjeId, Date zakazanDatum,  UUID lbp) {
        return repository.findByOdeljenjeIdAndZakazanDatumAndLbp(odeljenjeId, zakazanDatum, lbp);
    }
}
