package raf.si.bolnica.laboratory.services;

import org.w3c.dom.stylesheets.LinkStyle;
import raf.si.bolnica.laboratory.entities.LaboratorijskiRadniNalog;

import java.util.List;

public interface LaboratorijskiRadniNalogService {

    LaboratorijskiRadniNalog getRadniNalog(Long id);

    List<LaboratorijskiRadniNalog> getRadniNalozi();

    LaboratorijskiRadniNalog updateRadniNalog(LaboratorijskiRadniNalog radniNalog);

    LaboratorijskiRadniNalog saveRadniNalog(LaboratorijskiRadniNalog radniNalog);

    void deleteRadniNalog(Long id);



}
