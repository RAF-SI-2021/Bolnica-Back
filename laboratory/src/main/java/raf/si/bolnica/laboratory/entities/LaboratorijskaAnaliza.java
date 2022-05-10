package raf.si.bolnica.laboratory.entities;
import javax.persistence.*;

@Entity
public class LaboratorijskaAnaliza {

    @Id
    @Column(name = "laboratorijska_analiza_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long laboratorijskaAnalizaId;

    @Column(nullable = false)
    private String nazivAnalize;

    @Column(nullable = false)
    private String skracenica;

    public long getLaboratorijskaAnalizaId() {
        return laboratorijskaAnalizaId;
    }

    public void setLaboratorijskaAnalizaId(long laboratorijskaAnalizaId) {
        this.laboratorijskaAnalizaId = laboratorijskaAnalizaId;
    }

    public String getNazivAnalize() {
        return nazivAnalize;
    }

    public void setNazivAnalize(String nazivAnalize) {
        this.nazivAnalize = nazivAnalize;
    }

    public String getSkracenica() {
        return skracenica;
    }

    public void setSkracenica(String skracenica) {
        this.skracenica = skracenica;
    }
}