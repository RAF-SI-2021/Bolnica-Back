package raf.si.bolnica.management.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Hospitalizacija {

    @Id
    @Column(name = "hospitalizacija_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hospitalizacijaId;

    @Column(nullable = false)
    private long bolnickaSobaId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbpPacijenta;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzDodeljenogLekara;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzRegistratora;

    @Column(nullable = false)
    private Timestamp datumVremePrijema;

    private Timestamp datumVremeOtpustanja;

    @Column(nullable = false)
    private String uputnaDijagnoza;

    private String napomena;


    public long getHospitalizacijaId() {
        return hospitalizacijaId;
    }

    public void setHospitalizacijaId(long hospitalizacijaId) {
        this.hospitalizacijaId = hospitalizacijaId;
    }

    public UUID getLbpPacijenta() {
        return lbpPacijenta;
    }

    public void setLbpPacijenta(UUID lbpPacijenta) {
        this.lbpPacijenta = lbpPacijenta;
    }

    public UUID getLbzDodeljenogLekara() {
        return lbzDodeljenogLekara;
    }

    public void setLbzDodeljenogLekara(UUID lbzDodeljenogLekara) {
        this.lbzDodeljenogLekara = lbzDodeljenogLekara;
    }

    public UUID getLbzRegistratora() {
        return lbzRegistratora;
    }

    public void setLbzRegistratora(UUID lbzRegistratora) {
        this.lbzRegistratora = lbzRegistratora;
    }

    public Timestamp getDatumVremePrijema() {
        return datumVremePrijema;
    }

    public void setDatumVremePrijema(Timestamp datumVremePrijema) {
        this.datumVremePrijema = datumVremePrijema;
    }

    public Timestamp getDatumVremeOtpustanja() {
        return datumVremeOtpustanja;
    }

    public void setDatumVremeOtpustanja(Timestamp datumVremeOtpustanja) {
        this.datumVremeOtpustanja = datumVremeOtpustanja;
    }

    public long getBolnickaSobaId() {
        return bolnickaSobaId;
    }

    public void setBolnickaSobaId(long bolnickaSobaId) {
        this.bolnickaSobaId = bolnickaSobaId;
    }

    public String getUputnaDijagnoza() {
        return uputnaDijagnoza;
    }

    public void setUputnaDijagnoza(String uputnaDijagnoza) {
        this.uputnaDijagnoza = uputnaDijagnoza;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
