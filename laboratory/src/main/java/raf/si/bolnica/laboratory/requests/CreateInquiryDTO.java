package raf.si.bolnica.laboratory.requests;

import org.hibernate.annotations.Type;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.UUID;

public class CreateInquiryDTO {
    //neophodni parametri
    private TipUputa tip;
    private UUID lbz;
    private Integer izOdeljenjaId;
    private Integer zaOdeljenjeId;
    private UUID lbp;
    private Timestamp datumVremeKreiranja;


    //opcioni parametri
    private String zahtevaneAnalize;
    private String komentar;
    private String uputnaDijagnoza;
    private String razlogUpucivanja;

    public TipUputa getTip() {
        return tip;
    }

    public void setTip(TipUputa tip) {
        this.tip = tip;
    }

    public UUID getLbz() {
        return lbz;
    }

    public void setLbz(UUID lbz) {
        this.lbz = lbz;
    }

    public Integer getIzOdeljenjaId() {
        return izOdeljenjaId;
    }

    public void setIzOdeljenjaId(Integer izOdeljenjaId) {
        this.izOdeljenjaId = izOdeljenjaId;
    }

    public Integer getZaOdeljenjeId() {
        return zaOdeljenjeId;
    }

    public void setZaOdeljenjeId(Integer zaOdeljenjeId) {
        this.zaOdeljenjeId = zaOdeljenjeId;
    }

    public UUID getLbp() {
        return lbp;
    }

    public void setLbp(UUID lbp) {
        this.lbp = lbp;
    }

    public Timestamp getDatumVremeKreiranja() {
        return datumVremeKreiranja;
    }

    public void setDatumVremeKreiranja(Timestamp datumVremeKreiranja) {
        this.datumVremeKreiranja = datumVremeKreiranja;
    }

    public String getZahtevaneAnalize() {
        return zahtevaneAnalize;
    }

    public void setZahtevaneAnalize(String zahtevaneAnalize) {
        this.zahtevaneAnalize = zahtevaneAnalize;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getUputnaDijagnoza() {
        return uputnaDijagnoza;
    }

    public void setUputnaDijagnoza(String uputnaDijagnoza) {
        this.uputnaDijagnoza = uputnaDijagnoza;
    }

    public String getRazlogUpucivanja() {
        return razlogUpucivanja;
    }

    public void setRazlogUpucivanja(String razlogUpucivanja) {
        this.razlogUpucivanja = razlogUpucivanja;
    }
}
