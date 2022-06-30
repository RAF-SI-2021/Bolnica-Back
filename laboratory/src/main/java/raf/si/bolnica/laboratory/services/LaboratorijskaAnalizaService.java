package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.LaboratorijskaAnaliza;

import java.util.List;

public interface LaboratorijskaAnalizaService {

    LaboratorijskaAnaliza getLaboratorijskaAnaliza(Long id);

    List<LaboratorijskaAnaliza> getLaboratorijskeAnalize();

    List<LaboratorijskaAnaliza> getLaboratorijskaAnalizaBySkracenica(String skracenica);

    LaboratorijskaAnaliza updateLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza);

    LaboratorijskaAnaliza saveLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza);

    void deleteLaboratorijskaAnaliza(Long id);



}
