package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.entities.ParametarAnalize;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalizeKey;

import java.util.List;

public interface RezultatParametraAnalizeService {

    RezultatParametraAnalize getRezultatParametraAnalize(RezultatParametraAnalizeKey id);

    List<RezultatParametraAnalize> getRezultateParametaraAnalize();

    RezultatParametraAnalize updateRezultatParametraAnalize(RezultatParametraAnalize rezultatParametraAnalize);

    RezultatParametraAnalize saveRezultatParametraAnalize(RezultatParametraAnalize rezultatParametraAnalize);

    void deleteRezultatParametraAnalize(Long id);

}
