package raf.si.bolnica.management.services.bolnickaSoba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.BolnickaSoba;
import raf.si.bolnica.management.repositories.BolnickaSobaRepository;

import java.util.List;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class BolnickaSobaServiceImpl implements BolnickaSobaService {

    @Autowired
    BolnickaSobaRepository bolnickaSobaRepository;

    @Override
    public BolnickaSoba findById(long id) {

        return bolnickaSobaRepository.getByBolnickaSobaId(id);
    }

    @Override
    @Transactional()
    public BolnickaSoba save(BolnickaSoba bolnickaSoba) {
        return bolnickaSobaRepository.save(bolnickaSoba);
    }

    @Override
    public List<BolnickaSoba> findAllByDepartmentId(long departmentId) {
        return bolnickaSobaRepository.getBolnickaSobaByOdeljenjeId(departmentId);
    }

    @Override
    @Transactional()
    public synchronized int decrement(BolnickaSoba bolnickaSoba) {
        bolnickaSoba.setPopunjenost(bolnickaSoba.getPopunjenost() - 1);
        return bolnickaSoba.getPopunjenost();
    }

    @Override
    @Transactional()
    public synchronized int increment(BolnickaSoba bolnickaSoba) {
        bolnickaSoba.setPopunjenost(bolnickaSoba.getPopunjenost() + 1);
        return bolnickaSoba.getPopunjenost();
    }
}
