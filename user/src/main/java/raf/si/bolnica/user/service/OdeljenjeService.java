package raf.si.bolnica.user.service;

import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;

import java.util.List;

public interface OdeljenjeService {

    Odeljenje fetchOdeljenjeById(long id);

    List<Odeljenje> findAll();

    List<Odeljenje> findAllByPbb();

    List<ZdravstvenaUstanova> findAllHospitals();

    Odeljenje saveOdeljenje(Odeljenje odeljenje);

    List<Odeljenje> searchByNaziv(String naziv);
}
