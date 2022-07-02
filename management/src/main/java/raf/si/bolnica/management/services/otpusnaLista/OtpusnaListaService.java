package raf.si.bolnica.management.services.otpusnaLista;

import raf.si.bolnica.management.entities.OtpusnaLista;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface OtpusnaListaService {

    OtpusnaLista save(OtpusnaLista otpusnaLista);

    List<OtpusnaLista> findByLBP(UUID lbp);

    List<OtpusnaLista> findByLBPAndDate(UUID lbp, Date date);

    List<OtpusnaLista> findByLBPAndBetweenDates(UUID lbp, Date start, Date end);
}
