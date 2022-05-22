package raf.si.bolnica.laboratory.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class RezultatParametraAnalize {

    @EmbeddedId
    private RezultatParametraAnalizeKey id = new RezultatParametraAnalizeKey();

    @ManyToOne
    @MapsId("laboratorijskiRadniNalogId")
    @JoinColumn(name = "laboratorijski_radni_nalog_id")
    private LaboratorijskiRadniNalog laboratorijskiRadniNalog;

    @ManyToOne
    @MapsId("parametarAnalizeId")
    @JoinColumn(name = "parametar_analize_id")
    private ParametarAnalize parametarAnalize;

    private String rezultat = null;

    private Timestamp datumVreme = null;

    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID lbz = null;


    public RezultatParametraAnalizeKey getId() {
        return id;
    }

    public void setId(RezultatParametraAnalizeKey id) {
        this.id = id;
    }

    public LaboratorijskiRadniNalog getLaboratorijskiRadniNalog() {
        return laboratorijskiRadniNalog;
    }

    public void setLaboratorijskiRadniNalog(LaboratorijskiRadniNalog laboratorijskiRadniNalog) {
        this.laboratorijskiRadniNalog = laboratorijskiRadniNalog;
    }

    public ParametarAnalize getParametarAnalize() {
        return parametarAnalize;
    }

    public void setParametarAnalize(ParametarAnalize parametarAnalize) {
        this.parametarAnalize = parametarAnalize;
    }

    public String getRezultat() {
        return rezultat;
    }

    public void setRezultat(String rezultat) {
        this.rezultat = rezultat;
    }

    public Timestamp getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Timestamp datumVreme) {
        this.datumVreme = datumVreme;
    }

    public UUID getLbz() {
        return lbz;
    }

    public void setLbz(UUID lbz) {
        this.lbz = lbz;
    }
}
