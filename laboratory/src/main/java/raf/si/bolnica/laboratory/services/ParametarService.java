package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.Parametar;

import java.util.List;

public interface ParametarService {

    Parametar getParametar(Long id);

    List<Parametar> getParametri();

    Parametar updateParametar(Parametar parametar);

    Parametar saveParametar(Parametar parametar);

    void deleteParametar(Long id);

}
