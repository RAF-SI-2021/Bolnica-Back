package raf.si.bolnica.management.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
public class LekarskiIzvestajStacionar {

    @Id
    @Column(name = "stanje_pacijenta_ud")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stanjePacijentaId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbpPacijenta;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzLekaraSpecijaliste;

    @Column(nullable = false)
    private Date datumVremeKreiranja;

    private boolean indikatorPoverljivosti = false;

    @Column(nullable = false)
    private String objektivniNalaz;

    private String dijagnoza;

    private String predlozenaTerapija;

    private String savet;

    private boolean obrisan = false;

    public long getStanjePacijentaId() {
        return stanjePacijentaId;
    }

    public void setStanjePacijentaId(long stanjePacijentaId) {
        this.stanjePacijentaId = stanjePacijentaId;
    }

    public UUID getLbpPacijenta() {
        return lbpPacijenta;
    }

    public void setLbpPacijenta(UUID lbpPacijenta) {
        this.lbpPacijenta = lbpPacijenta;
    }

    public UUID getLbzLekaraSpecijaliste() {
        return lbzLekaraSpecijaliste;
    }

    public void setLbzLekaraSpecijaliste(UUID lbzLekaraSpecijaliste) {
        this.lbzLekaraSpecijaliste = lbzLekaraSpecijaliste;
    }

    public Date getDatumVremeKreiranja() {
        return datumVremeKreiranja;
    }

    public void setDatumVremeKreiranja(Date datumVremeKreiranja) {

        this.datumVremeKreiranja = datumVremeKreiranja;
    }

    public boolean isIndikatorPoverljivosti() {
        return indikatorPoverljivosti;
    }

    public void setIndikatorPoverljivosti(boolean indikatorPoverljivosti) {
        this.indikatorPoverljivosti = indikatorPoverljivosti;
    }

    public String getObjektivniNalaz() {
        return objektivniNalaz;
    }

    public void setObjektivniNalaz(String objektivniNalaz) {
        this.objektivniNalaz = objektivniNalaz;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public String getPredlozenaTerapija() {
        return predlozenaTerapija;
    }

    public void setPredlozenaTerapija(String predlozenaTerapija) {
        this.predlozenaTerapija = predlozenaTerapija;
    }

    public String getSavet() {
        return savet;
    }

    public void setSavet(String savet) {
        this.savet = savet;
    }

    public boolean isObrisan() {
        return obrisan;
    }

    public void setObrisan(boolean obrisan) {
        this.obrisan = obrisan;
    }
}
