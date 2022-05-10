package raf.si.bolnica.laboratory.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RezultatParametraAnalizeKey implements Serializable {

    @Column(name = "laboratorijski_radni_nalog_id")
    private long laboratorijskiRadniNalogId;

    @Column(name = "parametar_analize_id")
    private long parametarAnalizeId;

    public long getLaboratorijskiRadniNalogId() {
        return laboratorijskiRadniNalogId;
    }

    public void setLaboratorijskiRadniNalogId(long laboratorijskiRadniNalogId) {
        this.laboratorijskiRadniNalogId = laboratorijskiRadniNalogId;
    }

    public long getParametarAnalizeId() {
        return parametarAnalizeId;
    }

    public void setParametarAnalizeId(long parametarAnalizeId) {
        this.parametarAnalizeId = parametarAnalizeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RezultatParametraAnalizeKey that = (RezultatParametraAnalizeKey) o;
        return laboratorijskiRadniNalogId == that.laboratorijskiRadniNalogId && parametarAnalizeId == that.parametarAnalizeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(laboratorijskiRadniNalogId, parametarAnalizeId);
    }
}
