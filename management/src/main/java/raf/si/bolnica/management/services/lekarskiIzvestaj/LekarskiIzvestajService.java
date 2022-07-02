package raf.si.bolnica.management.services.lekarskiIzvestaj;

import raf.si.bolnica.management.entities.LekarskiIzvestajStacionar;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface LekarskiIzvestajService {

    LekarskiIzvestajStacionar save(LekarskiIzvestajStacionar lekarskiIzvestajStacionar);

    List<LekarskiIzvestajStacionar> findByLBP(UUID lbp, boolean indikator);

    List<LekarskiIzvestajStacionar> findByLBPAndDate(UUID lbp, Date date, boolean indikator);

    List<LekarskiIzvestajStacionar> findByLBPAndBetweenDates(UUID lbp, Date start, Date end, boolean indikator);
}
