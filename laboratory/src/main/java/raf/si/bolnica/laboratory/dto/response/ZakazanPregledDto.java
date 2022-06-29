package raf.si.bolnica.laboratory.dto.response;

import lombok.Data;
import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;
import raf.si.bolnica.laboratory.entities.enums.StatusPregleda;

import java.util.Date;
import java.util.UUID;

@Data
public class ZakazanPregledDto {

    private Long zakazanLaboratorijskiPregledId;
    private Integer odeljenjeId;
    private UUID lbp;
    private UUID lbz;
    private Date zakazanDatum;
    private StatusPregleda statusPregleda;
    private String napomena;

    public ZakazanPregledDto(ZakazanLaboratorijskiPregled zakazanLaboratorijskiPregled) {
        this.zakazanLaboratorijskiPregledId = zakazanLaboratorijskiPregled.getZakazanLaboratorijskiPregledId();
        this.odeljenjeId = zakazanLaboratorijskiPregled.getOdeljenjeId();
        this.lbp = zakazanLaboratorijskiPregled.getLbp();
        this.lbz = zakazanLaboratorijskiPregled.getLbz();
        this.zakazanDatum = zakazanLaboratorijskiPregled.getZakazanDatum();
        this.statusPregleda = zakazanLaboratorijskiPregled.getStatusPregleda();
        this.napomena = zakazanLaboratorijskiPregled.getNapomena();
    }
}
