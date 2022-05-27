package raf.si.bolnica.laboratory.entities;

import org.hibernate.annotations.Type;
import raf.si.bolnica.laboratory.entities.enums.StatusPregleda;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
public class ZakazanLaboratorijskiPregled {

    @Id
    @Column(name = "zakazan_laboratorijski_pregled_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long zakazanLaboratorijskiPregledId;

    @Column(nullable = false)
    private Integer odeljenjeId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbp;

    @Column(nullable = false)
    private Date zakazanDatum;

    @Enumerated(EnumType.STRING)
    private StatusPregleda statusPregleda;

    private String napomena;

    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbz;

    public long getZakazanLaboratorijskiPregledId() {
        return zakazanLaboratorijskiPregledId;
    }

    public void setZakazanLaboratorijskiPregledId(long zakazanLaboratorijskiPregledId) {
        this.zakazanLaboratorijskiPregledId = zakazanLaboratorijskiPregledId;
    }

    public Integer getOdeljenjeId() {
        return odeljenjeId;
    }

    public void setOdeljenjeId(Integer odeljenjeId) {
        this.odeljenjeId = odeljenjeId;
    }

    public UUID getLbp() {
        return lbp;
    }

    public void setLbp(UUID lbp) {
        this.lbp = lbp;
    }

    public Date getZakazanDatum() {
        return zakazanDatum;
    }

    public void setZakazanDatum(Date zakazanDatum) {
        this.zakazanDatum = zakazanDatum;
    }

    public StatusPregleda getStatusPregleda() {
        return statusPregleda;
    }

    public void setStatusPregleda(StatusPregleda statusPregleda) {
        this.statusPregleda = statusPregleda;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public UUID getLbz() {
        return lbz;
    }

    public void setLbz(UUID lbz) {
        this.lbz = lbz;
    }
}
