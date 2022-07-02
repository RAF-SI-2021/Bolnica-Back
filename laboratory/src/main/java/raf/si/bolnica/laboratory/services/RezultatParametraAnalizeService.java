package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.RezultatParametraAnalize;
import raf.si.bolnica.laboratory.entities.RezultatParametraAnalizeKey;
import raf.si.bolnica.laboratory.entities.*;

import java.util.List;

public interface RezultatParametraAnalizeService {

    RezultatParametraAnalize getRezultatParametraAnalize(RezultatParametraAnalizeKey id);

    List<RezultatParametraAnalize> getRezultateParametaraAnalize();

    List<RezultatParametraAnalize> getRezultateParametaraAnalizeByRadniNalog(LaboratorijskiRadniNalog labRadniNalog);

    RezultatParametraAnalize updateRezultatParametraAnalize(RezultatParametraAnalize rezultatParametraAnalize);

    RezultatParametraAnalize saveRezultatParametraAnalize(RezultatParametraAnalize rezultatParametraAnalize);

    void deleteRezultatParametraAnalize(Long id);

}
