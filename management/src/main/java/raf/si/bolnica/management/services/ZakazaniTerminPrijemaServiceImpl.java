package raf.si.bolnica.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raf.si.bolnica.management.entities.ZakazaniTerminPrijema;
import raf.si.bolnica.management.entities.enums.StatusTermina;
import raf.si.bolnica.management.repositories.ZakazaniTerminPrijemaRepository;

import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class ZakazaniTerminPrijemaServiceImpl implements ZakazaniTerminPrijemaService {

    @Autowired
    ZakazaniTerminPrijemaRepository repository;


    @Override
    @Transactional()
    public ZakazaniTerminPrijema save(ZakazaniTerminPrijema zakazaniTerminPrijema) {
        return repository.save(zakazaniTerminPrijema);
    }

    @Override
    public List<ZakazaniTerminPrijema> getAll(long odeljenjeId, Date datum, UUID lbp) {

        List<ZakazaniTerminPrijema> result;

        if (lbp != null) {
            result = repository.findAllByOdeljenjeIdAndLbpPacijentaEquals(odeljenjeId, lbp);
        } else {
            result = repository.findAllByOdeljenjeId(odeljenjeId);
        }

        if (datum != null) {
            result = result
                    .stream()
                    .filter(p -> datum.equals(new Date(p.getDatumVremePrijema().getTime())))
                    .collect(Collectors.toList());
        }

        return result;
    }

    @Override
    @Transactional()
    public void setStatus(long id, StatusTermina status) {
        ZakazaniTerminPrijema zakazaniTerminPrijema = repository.getOne(id);
        zakazaniTerminPrijema.setStatusTermina(status);

        repository.save(zakazaniTerminPrijema);
    }
}
