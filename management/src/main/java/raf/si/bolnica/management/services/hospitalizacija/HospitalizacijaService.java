package raf.si.bolnica.management.services.hospitalizacija;

import raf.si.bolnica.management.entities.Hospitalizacija;

import java.util.UUID;

public interface HospitalizacijaService {

    Hospitalizacija save(Hospitalizacija hospitalizacija);
    Hospitalizacija findCurrentByLbp(UUID lbp);
}
