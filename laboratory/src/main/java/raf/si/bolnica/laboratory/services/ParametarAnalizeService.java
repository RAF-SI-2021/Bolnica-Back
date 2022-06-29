package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;
import raf.si.bolnica.laboratory.entities.ParametarAnalize;

import java.util.List;

public interface ParametarAnalizeService {

    ParametarAnalize getParametarAnalize(Long id);

    List<ParametarAnalize> getParametriAnalize();

    ParametarAnalize updateParametarAnalize(ParametarAnalize parametarAnalize);

    ParametarAnalize saveParametarAnalize(ParametarAnalize parametarAnalize);

    void deleteParametarAnalize(Long id);

    List<ParametarAnalize> getParametarAnalizeByLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza);
}
