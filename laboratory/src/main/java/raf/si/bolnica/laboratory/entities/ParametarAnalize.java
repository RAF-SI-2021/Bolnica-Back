package raf.si.bolnica.laboratory.entities;

import javax.persistence.*;

@Entity
public class ParametarAnalize {

    @Id
    @Column(name = "parametar_analize_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long parametarAnalizeId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "laboratorijska_analiza_id")
    private LaboratorijskaAnaliza laboratorijskaAnaliza;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parametar_id")
    private Parametar parametar;

    public long getParametarAnalizeId() {
        return parametarAnalizeId;
    }

    public void setParametarAnalizeId(long parametarAnalizeId) {
        this.parametarAnalizeId = parametarAnalizeId;
    }

    public LaboratorijskaAnaliza getLaboratorijskaAnaliza() {
        return laboratorijskaAnaliza;
    }

    public void setLaboratorijskaAnaliza(LaboratorijskaAnaliza laboratorijskaAnaliza) {
        this.laboratorijskaAnaliza = laboratorijskaAnaliza;
    }

    public Parametar getParametar() {
        return parametar;
    }

    public void setParametar(Parametar parametar) {
        this.parametar = parametar;
    }
}
