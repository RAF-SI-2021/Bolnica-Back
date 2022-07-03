package raf.si.bolnica.management.services.otpusnaLista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.OtpusnaLista;
import raf.si.bolnica.management.repositories.OtpusnaListaRepository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;


@Service
@Transactional(value = "transactionManager", readOnly = true)
public class OtpusnaListaServiceImpl implements OtpusnaListaService {

    @Autowired
    OtpusnaListaRepository otpusnaListaRepository;

    @Override
    @Transactional()
    public OtpusnaLista save(OtpusnaLista otpusnaLista) {
        return otpusnaListaRepository.save(otpusnaLista);
    }

    @Override
    public List<OtpusnaLista> findByLBP(UUID lbp) {
        return otpusnaListaRepository.findAllByLbpPacijenta(lbp);
    }

    @Override
    public List<OtpusnaLista> findByLBPAndDate(UUID lbp, Date date) {
        return otpusnaListaRepository.findAllByLbpPacijentaAndDatumVremeKreiranja(lbp, date);
    }

    @Override
    public List<OtpusnaLista> findByLBPAndBetweenDates(UUID lbp, Date start, Date end) {
        return otpusnaListaRepository.findAllByLbpPacijentaAndDatumVremeKreiranjaGreaterThanEqualAndDatumVremeKreiranjaLessThanEqual(lbp, start, end);
    }
}
