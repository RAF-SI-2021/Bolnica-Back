package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.Uput;

import java.util.List;

public interface UputService {

    Uput getUput(Long id);

    Uput fetchUputById(Long id);

    List<Uput> getUputi();

    Uput saveUput(Uput uput);

    Uput updateUput(Uput uput);

    void deleteUput(Long id);



}
