package raf.si.bolnica.management.entities;

import raf.si.bolnica.management.entities.enums.PrispecePacijenta;
import raf.si.bolnica.management.entities.enums.StatusPregleda;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

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
    private UUID lBZLekara;

    @Column(nullable = false)
    private UUID LBZSestre;

    @ManyToOne
    @JoinColumn(name = "pacijent_id")
    private Pacijent pacijent;

    public UUID getLBZLekara() {
        return lBZLekara;
    }

    public long getZakazaniPregledId() {
        return zakazaniPregledId;
    }

    public UUID getLBZSestre() {
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

    public void setLBZLekara(UUID LBZLekara) {
        this.lBZLekara = LBZLekara;
    }

    public void setLBZSestre(UUID LBZSestre) {
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
