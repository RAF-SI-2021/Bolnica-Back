package raf.si.bolnica.laboratory.services;

import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface ZakazanLaboratorijskiPregledService {

    ZakazanLaboratorijskiPregled saveZakazanPregled(ZakazanLaboratorijskiPregled pregled);

    ZakazanLaboratorijskiPregled getZakazanPregled(Long id);

    List<ZakazanLaboratorijskiPregled> getZakazaniPregledi();

    void deleteZakazanPregled(Long id);

    ZakazanLaboratorijskiPregled updateZakazanPregled(ZakazanLaboratorijskiPregled pregled);

    List<ZakazanLaboratorijskiPregled> getZakazaniPreglediByDate(Date date);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeId(Integer odeljenjeId);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndZakazanDatum(Integer odeljenjeId, Date zakazanDatum);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndLbp(Integer odeljenjeId, UUID lbp);
    List<ZakazanLaboratorijskiPregled> findByOdeljenjeIdAndZakazanDatumAndLbp(Integer odeljenjeId, Date zakazanDatum, UUID lbp);

}
