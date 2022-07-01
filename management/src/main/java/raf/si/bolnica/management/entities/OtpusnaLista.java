package raf.si.bolnica.management.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
public class OtpusnaLista {

    @Id
    @Column(name = "otpusna_lista_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long otpusnaListaId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbpPacijenta;

    @Column(nullable = false)
    private long hospitalizacijaId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzOrdinirajucegLekara;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzNacelnikOdeljenja;

    private String prateceDijagnoze;

    @Column(nullable = false)
    private String anamneza;

    private String analize;

    private String tokBolesti;

    @Column(nullable = false)
    private String zakljucak;

    private String terapija;

    @Column(nullable = false)
    private Date datumVremeKreiranja;

    public long getOtpusnaListaId() {
        return otpusnaListaId;
    }

    public void setOtpusnaListaId(long otpusnaListaId) {
        this.otpusnaListaId = otpusnaListaId;
    }

    public UUID getLbpPacijenta() {
        return lbpPacijenta;
    }

    public void setLbpPacijenta(UUID lbpPacijenta) {
        this.lbpPacijenta = lbpPacijenta;
    }

    public long getHospitalizacijaId() {
        return hospitalizacijaId;
    }

    public void setHospitalizacijaId(long hospitalizacijaId) {
        this.hospitalizacijaId = hospitalizacijaId;
    }

    public UUID getLbzOrdinirajucegLekara() {
        return lbzOrdinirajucegLekara;
    }

    public void setLbzOrdinirajucegLekara(UUID lbzOrdinirajucegLekara) {
        this.lbzOrdinirajucegLekara = lbzOrdinirajucegLekara;
    }

    public UUID getLbzNacelnikOdeljenja() {
        return lbzNacelnikOdeljenja;
    }

    public void setLbzNacelnikOdeljenja(UUID lbzNacelnikOdeljenja) {
        this.lbzNacelnikOdeljenja = lbzNacelnikOdeljenja;
    }

    public String getPrateceDijagnoze() {
        return prateceDijagnoze;
    }

    public void setPrateceDijagnoze(String prateceDijagnoze) {
        this.prateceDijagnoze = prateceDijagnoze;
    }

    public String getAnamneza() {
        return anamneza;
    }

    public void setAnamneza(String anamneza) {
        this.anamneza = anamneza;
    }

    public String getAnalize() {
        return analize;
    }

    public void setAnalize(String analize) {
        this.analize = analize;
    }

    public String getTokBolesti() {
        return tokBolesti;
    }

    public void setTokBolesti(String tokBolesti) {
        this.tokBolesti = tokBolesti;
    }

    public String getZakljucak() {
        return zakljucak;
    }

    public void setZakljucak(String zakljucak) {
        this.zakljucak = zakljucak;
    }

    public String getTerapija() {
        return terapija;
    }

    public void setTerapija(String terapija) {
        this.terapija = terapija;
    }

    public Date getDatumVremeKreiranja() {
        return datumVremeKreiranja;
    }

    public void setDatumVremeKreiranja(Date datumVremeKreiranja) {
        this.datumVremeKreiranja = datumVremeKreiranja;
    }
}
