package raf.si.bolnica.management.entities;

import raf.si.bolnica.management.entities.enums.StatusTermina;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class ZakazaniTerminPrijema {

    @Id
    @Column(name = "zakazani_termin_prijema_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long zakazaniTerminPrijemaId;

    @Column(nullable = false)
    private long odeljenjeId;

    @Column(nullable = false)
    private UUID lbpPacijenta;

    @Column(nullable = false)
    private UUID lbzZaposlenog;

    @Column(nullable = false)
    private Timestamp datumVremePrijema;

    @Enumerated(EnumType.STRING)
    private StatusTermina statusTermina = StatusTermina.ZAKAZAN;

    private String napomena;

    public long getZakazaniTerminPrijemaId() {
        return zakazaniTerminPrijemaId;
    }

    public void setZakazaniTerminPrijemaId(long zakazaniTerminPrijemaId) {
        this.zakazaniTerminPrijemaId = zakazaniTerminPrijemaId;
    }

    public long getOdeljenjeId() {
        return odeljenjeId;
    }

    public void setOdeljenjeId(long odeljenjeId) {
        this.odeljenjeId = odeljenjeId;
    }

    public UUID getLbpPacijenta() {
        return lbpPacijenta;
    }

    public void setLbpPacijenta(UUID lbpPacijenta) {
        this.lbpPacijenta = lbpPacijenta;
    }

    public UUID getLbzZaposlenog() {
        return lbzZaposlenog;
    }

    public void setLbzZaposlenog(UUID lbzZaposlenog) {
        this.lbzZaposlenog = lbzZaposlenog;
    }

    public Timestamp getDatumVremePrijema() {
        return datumVremePrijema;
    }

    public void setDatumVremePrijema(Timestamp datumVremePrijema) {
        this.datumVremePrijema = datumVremePrijema;
    }

    public StatusTermina getStatusTermina() {
        return statusTermina;
    }

    public void setStatusTermina(StatusTermina statusTermina) {
        this.statusTermina = statusTermina;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
