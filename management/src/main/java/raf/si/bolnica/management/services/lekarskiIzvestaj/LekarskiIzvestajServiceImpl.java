package raf.si.bolnica.management.services.lekarskiIzvestaj;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raf.si.bolnica.management.entities.LekarskiIzvestajStacionar;
import raf.si.bolnica.management.repositories.LekarskiIzvestajRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(value = "transactionManager", readOnly = true)
public class LekarskiIzvestajServiceImpl implements LekarskiIzvestajService {

    @Autowired
    LekarskiIzvestajRepository lekarskiIzvestajRepository;

    @Override
    @Transactional()
    public LekarskiIzvestajStacionar save(LekarskiIzvestajStacionar lekarskiIzvestajStacionar) {
        return lekarskiIzvestajRepository.save(lekarskiIzvestajStacionar);
    }

    @Override
    public List<LekarskiIzvestajStacionar> findByLBP(UUID lbp, boolean indikator) {
        List<LekarskiIzvestajStacionar> lis = new ArrayList<>();
        for (LekarskiIzvestajStacionar l : lekarskiIzvestajRepository.findAllByLbpPacijenta(lbp)) {
            if (!l.isObrisan()) {
                if (l.isIndikatorPoverljivosti()) {
                    if (indikator) {
                        lis.add(l);
                    }
                } else {
                    lis.add(l);
                }

            }
        }
        return lis;
    }

    @Override
    public List<LekarskiIzvestajStacionar> findByLBPAndDate(UUID lbp, Date date, boolean indikator) {
        List<LekarskiIzvestajStacionar> lekarskiIzvestajStacionars = new ArrayList<>();
        for (LekarskiIzvestajStacionar l : lekarskiIzvestajRepository.findAllByLbpPacijentaAndDatumVremeKreiranja(lbp, date)) {
            if (!l.isObrisan()) {
                if (l.isIndikatorPoverljivosti()) {
                    if (indikator) {
                        lekarskiIzvestajStacionars.add(l);
                    }
                } else {
                    lekarskiIzvestajStacionars.add(l);
                }
            }
        }
        return lekarskiIzvestajStacionars;
    }

    @Override
    public List<LekarskiIzvestajStacionar> findByLBPAndBetweenDates(UUID lbp, Date start, Date end, boolean indikator) {
        List<LekarskiIzvestajStacionar> lekarskiIzvestajStacionars = new ArrayList<>();
        for (LekarskiIzvestajStacionar l : lekarskiIzvestajRepository.findAllByLbpPacijentaAndDatumVremeKreiranjaGreaterThanEqualAndDatumVremeKreiranjaLessThanEqual(lbp, start, end)) {
            if (!l.isObrisan()) {
                if (l.isIndikatorPoverljivosti()) {
                    if (indikator) {
                        lekarskiIzvestajStacionars.add(l);
                    }
                } else {
                    lekarskiIzvestajStacionars.add(l);
                }
            }
        }
        return lekarskiIzvestajStacionars;
    }
}
