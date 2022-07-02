package raf.si.bolnica.laboratory.entities;

import org.hibernate.annotations.Type;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
public class LaboratorijskiRadniNalog {

    @Id
    @Column(name = "laboratorijski_radni_nalog_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long laboratorijskiRadniNalogId;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbp;

    @Column(nullable = false)
    private Timestamp datumVremeKreiranja;

    @Enumerated(EnumType.STRING)
    private StatusObrade statusObrade = StatusObrade.NEOBRADJEN;

    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzTehnicar;

    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbzBiohemicar = null;

    // Fks
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "uput")
    private Uput uput;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "laboratorijskiRadniNalog")
    private List<RezultatParametraAnalize> rezultati;

    public List<RezultatParametraAnalize> getRezultati() {
        return rezultati;
    }

    public void setRezultati(List<RezultatParametraAnalize> rezultati) {
        this.rezultati = rezultati;
    }

    @Lob
    private Blob labReport;

    public long getLaboratorijskiRadniNalogId() {
        return laboratorijskiRadniNalogId;
    }

    public void setLaboratorijskiRadniNalogId(long laboratorijskiRadniNalogId) {
        this.laboratorijskiRadniNalogId = laboratorijskiRadniNalogId;
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

    public StatusObrade getStatusObrade() {
        return statusObrade;
    }

    public void setStatusObrade(StatusObrade statusObrade) {
        this.statusObrade = statusObrade;
    }

    public UUID getLbzTehnicar() {
        return lbzTehnicar;
    }

    public void setLbzTehnicar(UUID lbzTehnicar) {
        this.lbzTehnicar = lbzTehnicar;
    }

    public UUID getLbzBiohemicar() {
        return lbzBiohemicar;
    }

    public void setLbzBiohemicar(UUID lbzBiohemicar) {
        this.lbzBiohemicar = lbzBiohemicar;
    }

    public Uput getUput() {
        return uput;
    }

    public void setUput(Uput uput) {
        this.uput = uput;
    }

    public Blob getLabReport() { return labReport; }

    public void setLabReport(Blob labReport) { this.labReport = labReport; }
}
