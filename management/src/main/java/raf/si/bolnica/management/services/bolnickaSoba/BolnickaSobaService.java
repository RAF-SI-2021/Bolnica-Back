package raf.si.bolnica.management.services.bolnickaSoba;


import raf.si.bolnica.management.entities.BolnickaSoba;

public interface BolnickaSobaService {

    BolnickaSoba findById(long id);
    BolnickaSoba save(BolnickaSoba bolnickaSoba);
    int decrement(BolnickaSoba bolnickaSoba);
    int increment(BolnickaSoba bolnickaSoba);
}
