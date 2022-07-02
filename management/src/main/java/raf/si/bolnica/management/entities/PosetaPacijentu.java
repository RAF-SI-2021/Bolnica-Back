package raf.si.bolnica.management.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class PosetaPacijentu implements Serializable {

    @Id
    @Column(name = "poseta_pacijentu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long posetaPacijentuId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbpPacijenta;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzRegistratora;

    @Column(nullable = false)
    private Timestamp datumVreme;

    @Column(nullable = false)
    private String imePosetioca;

    @Column(nullable = false)
    private String prezimePosetioca;

    @Column(nullable = false)
    private String jmbgPosetioca;

    private String napomena;

    public String getImePosetioca() {
        return imePosetioca;
    }

    public void setImePosetioca(String imePosetioca) {
        this.imePosetioca = imePosetioca;
    }

    public long getPosetaPacijentuId() {
        return posetaPacijentuId;
    }

    public void setPosetaPacijentuId(long posetaPacijentuId) {
        this.posetaPacijentuId = posetaPacijentuId;
    }

    public UUID getLbpPacijenta() {
        return lbpPacijenta;
    }

    public void setLbpPacijenta(UUID lbpPacijenta) {
        this.lbpPacijenta = lbpPacijenta;
    }

    public UUID getLbzRegistratora() {
        return lbzRegistratora;
    }

    public void setLbzRegistratora(UUID lbzRegistratora) {
        this.lbzRegistratora = lbzRegistratora;
    }

    public Timestamp getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Timestamp datumVreme) {
        this.datumVreme = datumVreme;
    }

    public String getPrezimePosetioca() {
        return prezimePosetioca;
    }

    public void setPrezimePosetioca(String prezimePosetioca) {
        this.prezimePosetioca = prezimePosetioca;
    }

    public String getJmbgPosetioca() {
        return jmbgPosetioca;
    }

    public void setJmbgPosetioca(String jmbgPosetioca) {
        this.jmbgPosetioca = jmbgPosetioca;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
