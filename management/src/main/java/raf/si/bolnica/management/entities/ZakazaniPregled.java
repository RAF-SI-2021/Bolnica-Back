package raf.si.bolnica.management.entities;

import net.bytebuddy.implementation.bind.annotation.Default;
import raf.si.bolnica.management.entities.enums.PrispecePacijenta;
import raf.si.bolnica.management.entities.enums.StatusPregleda;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class ZakazaniPregled {

    @Id
    @Column(name = "zakazani_pregled_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long zakazaniPregledId;

    @Column(nullable = false)
    private Timestamp datumIVremePregleda;

    @Enumerated(EnumType.STRING)
    private StatusPregleda statusPregleda = StatusPregleda.ZAKAZANO;

    @Enumerated(EnumType.STRING)
    private PrispecePacijenta prispecePacijenta = PrispecePacijenta.NIJE_DOSAO;

    private String Napomena;

    //FKs
    @Column(nullable = false)
    private long LBZLekara;

    @Column(nullable = false)
    private long LBZSestre;

    @ManyToOne
    @JoinColumn(name = "pacijent_id")
    private Pacijent pacijent;

    public long getLBZLekara() {
        return LBZLekara;
    }

    public long getZakazaniPregledId() {
        return zakazaniPregledId;
    }

    public long getLBZSestre() {
        return LBZSestre;
    }

    public Pacijent getPacijent() {
        return pacijent;
    }

    public PrispecePacijenta getPrispecePacijenta() {
        return prispecePacijenta;
    }

    public StatusPregleda getStatusPregleda() {
        return statusPregleda;
    }

    public String getNapomena() {
        return Napomena;
    }

    public Timestamp getDatumIVremePregleda() {
        return datumIVremePregleda;
    }

    public void setDatumIVremePregleda(Timestamp datumIVremePregleda) {
        this.datumIVremePregleda = datumIVremePregleda;
    }

    public void setLBZLekara(long LBZLekara) {
        this.LBZLekara = LBZLekara;
    }

    public void setLBZSestre(long LBZSestre) {
        this.LBZSestre = LBZSestre;
    }

    public void setNapomena(String napomena) {
        Napomena = napomena;
    }

    public void setPacijent(Pacijent pacijent) {
        this.pacijent = pacijent;
    }

    public void setPrispecePacijenta(PrispecePacijenta prispecePacijenta) {
        this.prispecePacijenta = prispecePacijenta;
    }

    public void setZakazaniPregledId(long zakazaniPregledId) {
        this.zakazaniPregledId = zakazaniPregledId;
    }

    public void setStatusPregleda(StatusPregleda statusPregleda) {
        this.statusPregleda = statusPregleda;
    }
    
}
