package raf.si.bolnica.laboratory.entities;


import raf.si.bolnica.laboratory.entities.enums.TipVrednosti;

import javax.persistence.*;

@Entity
public class Parametar {

    @Id
    @Column(name = "parametar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long parametarId;

    @Column(nullable = false)
    private String nazivParametra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipVrednosti tipVrednosti;

    private String jedinicaMere;

    private double donjaGranica;

    private double gornjaGranica;

    public long getParametarId() {
        return parametarId;
    }

    public void setParametarId(long parametarId) {
        this.parametarId = parametarId;
    }

    public String getNazivParametra() {
        return nazivParametra;
    }

    public void setNazivParametra(String nazivParametra) {
        this.nazivParametra = nazivParametra;
    }

    public TipVrednosti getTipVrednosti() {
        return tipVrednosti;
    }

    public void setTipVrednosti(TipVrednosti tipVrednosti) {
        this.tipVrednosti = tipVrednosti;
    }

    public String getJedinicaMere() {
        return jedinicaMere;
    }

    public void setJedinicaMere(String jedinicaMere) {
        this.jedinicaMere = jedinicaMere;
    }

    public double getDonjaGranica() {
        return donjaGranica;
    }

    public void setDonjaGranica(double donjaGranica) {
        this.donjaGranica = donjaGranica;
    }

    public double getGornjaGranica() {
        return gornjaGranica;
    }

    public void setGornjaGranica(double gornjaGranica) {
        this.gornjaGranica = gornjaGranica;
    }
}
