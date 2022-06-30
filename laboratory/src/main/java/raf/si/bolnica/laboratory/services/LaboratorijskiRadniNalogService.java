package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;

import java.util.List;

public interface LaboratorijskiRadniNalogService {

    LaboratorijskiRadniNalog getRadniNalog(Long id);

    LaboratorijskiRadniNalog fetchRadniNalogById(Long id);

    List<LaboratorijskiRadniNalog> getRadniNalozi();

    LaboratorijskiRadniNalog updateRadniNalog(LaboratorijskiRadniNalog radniNalog);

    LaboratorijskiRadniNalog saveRadniNalog(LaboratorijskiRadniNalog radniNalog);

    void deleteRadniNalog(Long id);



}
