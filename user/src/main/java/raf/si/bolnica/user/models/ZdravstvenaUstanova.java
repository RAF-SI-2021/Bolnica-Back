package raf.si.bolnica.user.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public class ZdravstvenaUstanova {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_bolnice;

    @Column(nullable = false, unique = true)
    private long poslovni_broj_bolnice;

    @Column(nullable = false)
    private String naziv;

    @Column(nullable = false)
    private String skracen_naziv;

    @Column(nullable = false)
    private String mesto;

    @Column(nullable = false)
    private String adresa;

    @Column(nullable = false)
    private String delatnost;

    @OneToMany(mappedBy = "bolnica")
    private List<Odeljenje> odeljenja;

    @Column(nullable = true)
    private boolean obrisan = false;

    @Column(nullable = false)
    private Date datumOsnivanja;

    public ZdravstvenaUstanova(){

    }

    public long getId_bolnice() {
        return id_bolnice;
    }

    public void setId_bolnice(long id_bolnice) {
        this.id_bolnice = id_bolnice;
    }

    public long getPoslovni_broj_bolnice() {
        return poslovni_broj_bolnice;
    }

    public void setPoslovni_broj_bolnice(long poslovni_broj_bolnice) {
        this.poslovni_broj_bolnice = poslovni_broj_bolnice;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getSkracen_naziv() {
        return skracen_naziv;
    }

    public void setSkracen_naziv(String skracen_naziv) {
        this.skracen_naziv = skracen_naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getDelatnost() {
        return delatnost;
    }

    public void setDelatnost(String delatnost) {
        this.delatnost = delatnost;
    }

    public List<Odeljenje> getOdeljenja() {
        return odeljenja;
    }

    public void setOdeljenja(List<Odeljenje> odeljenja) {
        this.odeljenja = odeljenja;
    }

    public boolean isObrisan() {
        return obrisan;
    }

    public void setObrisan(boolean obrisan) {
        this.obrisan = obrisan;
    }

    public Date getDatumOsnivanja() {
        return datumOsnivanja;
    }

    public void setDatumOsnivanja(Date datumOsnivanja) {
        this.datumOsnivanja = datumOsnivanja;
    }
}
