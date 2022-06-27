package raf.si.bolnica.management.services.otpusnaLista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.OtpusnaLista;
import raf.si.bolnica.management.repositories.OtpusnaListaRepository;


@Service
@Transactional("transactionManager")
public class OtpusnaListaServiceImpl implements OtpusnaListaService{

    @Autowired
    OtpusnaListaRepository otpusnaListaRepository;

    @Override
    public OtpusnaLista save(OtpusnaLista otpusnaLista) {
        return otpusnaListaRepository.save(otpusnaLista);
    }
}
