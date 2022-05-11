package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;

import java.util.List;

public interface ZakazanLaboratorijskiPregledService {

    ZakazanLaboratorijskiPregled saveZakazanPregled(ZakazanLaboratorijskiPregled pregled);

    ZakazanLaboratorijskiPregled getZakazanPregled(Long id);

    List<ZakazanLaboratorijskiPregled> getZakazaniPregledi();

    void deleteZakazanPregled(Long id);

    ZakazanLaboratorijskiPregled updateZakazanPregled(ZakazanLaboratorijskiPregled pregled);

}
