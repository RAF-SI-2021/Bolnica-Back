package raf.si.bolnica.user.service;

import raf.si.bolnica.user.models.Odeljenje;

import java.util.List;

public interface OdeljenjeService {

    Odeljenje fetchOdeljenjeById(long id);

    List<Odeljenje> findAll();

    List<Odeljenje> findAllByPbb(long pbb);

    Odeljenje saveOdeljenje(Odeljenje odeljenje);

    List<Odeljenje> searchByNaziv(String naziv);
}
