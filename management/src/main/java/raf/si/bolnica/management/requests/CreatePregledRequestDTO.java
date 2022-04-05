package raf.si.bolnica.management.requests;

import raf.si.bolnica.management.entities.enums.RezultatLecenja;

import javax.persistence.Column;
import java.sql.Date;
import java.util.UUID;

public class CreatePregledRequestDTO {

    private UUID lbp;

    private long zaposleniId;

    private Boolean indikatorPoverljivosti;

    private String glavneTegobe;

    private String sadasnjaBolest;

    private String licnaAnamneza;

    private String porodicnaAnamneza;

    private String misljenjePacijenta;

    private String objektivniNalaz;

    private String dijagnoza;

    private String predlozenaTerapija;

    private String savet;

    private RezultatLecenja rezultatLecenja;

    private String opisTekucgStanja;

    public UUID getLbp() {
        return lbp;
    }

    public long getZaposleniId() {
        return zaposleniId;
    }

    public String getGlavneTegobe() {
        return glavneTegobe;
    }

    public String getSadasnjaBolest() {
        return sadasnjaBolest;
    }

    public String getLicnaAnamneza() {
        return licnaAnamneza;
    }

    public String getPorodicnaAnamneza() {
        return porodicnaAnamneza;
    }

    public String getMisljenjePacijenta() {
        return misljenjePacijenta;
    }

    public String getObjektivniNalaz() {
        return objektivniNalaz;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public String getPredlozenaTerapija() {
        return predlozenaTerapija;
    }

    public String getSavet() {
        return savet;
    }

    public Boolean getIndikatorPoverljivosti() {
        return indikatorPoverljivosti;
    }

    public void setIndikatorPoverljivosti(Boolean indikatorPoverljivosti) {
        this.indikatorPoverljivosti = indikatorPoverljivosti;
    }

    public String getOpisTekucgStanja() {
        return opisTekucgStanja;
    }

    public RezultatLecenja getRezultatLecenja() {
        return rezultatLecenja;
    }
}
