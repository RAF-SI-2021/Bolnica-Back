package raf.si.bolnica.user.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Odeljenje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idOdeljenja;

    @Column(nullable = false)
    private long poslovniBrojOdeljenja;

    @Column(nullable = false)
    private String naziv;

    @ManyToOne
    private ZdravstvenaUstanova bolnica;

    @OneToMany(mappedBy = "odeljenje")
    private List<User> zaposleni;

    @Column(nullable = true)
    private boolean obrisan = false;

    public Odeljenje(){

    }

    public List<User> getZaposleni() {
        return zaposleni;
    }

    public void setZaposleni(List<User> zaposleni) {
        this.zaposleni = zaposleni;
    }

    public long getIdOdeljenja() {
        return idOdeljenja;
    }

    public void setIdOdeljenja(long idOdeljenja) {
        this.idOdeljenja = idOdeljenja;
    }

    public long getPoslovniBrojOdeljenja() {
        return poslovniBrojOdeljenja;
    }

    public void setPoslovniBrojOdeljenja(long poslovniBrojOdeljenja) {
        this.poslovniBrojOdeljenja = poslovniBrojOdeljenja;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ZdravstvenaUstanova getBolnica() {
        return bolnica;
    }

    public void setBolnica(ZdravstvenaUstanova bolnica) {
        this.bolnica = bolnica;
    }

    public boolean isObrisan() {
        return obrisan;
    }

    public void setObrisan(boolean obrisan) {
        this.obrisan = obrisan;
    }
}
