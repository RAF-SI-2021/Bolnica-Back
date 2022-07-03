package raf.si.bolnica.management.services.hospitalizacija;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.Hospitalizacija;
import raf.si.bolnica.management.repositories.HospitalizacijaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class HospitalizacijaServiceImpl implements HospitalizacijaService {

    @Autowired
    HospitalizacijaRepository hospitalizacijaRepository;


    @Override
    @Transactional()
    public Hospitalizacija save(Hospitalizacija hospitalizacija) {
        return hospitalizacijaRepository.save(hospitalizacija);
    }

    @Override
    public Hospitalizacija findCurrentByLbp(UUID lbp) {
        List<Hospitalizacija> hospitalizacijas = hospitalizacijaRepository.findAllByLbpPacijenta(lbp);
        for (Hospitalizacija h : hospitalizacijas) {
            if (h.getDatumVremeOtpustanja() == null) {
                return h;
            }
        }
        return null;
    }

    @Override
    public List<Hospitalizacija> findByLbp(UUID lbp) {
        return hospitalizacijaRepository.findAllByLbpPacijenta(lbp);
    }
}
