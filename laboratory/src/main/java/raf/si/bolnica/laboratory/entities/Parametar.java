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

    private int donjaGranica;

    private int gornjaGranica;

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

    public int getDonjaGranica() {
        return donjaGranica;
    }

    public void setDonjaGranica(int donjaGranica) {
        this.donjaGranica = donjaGranica;
    }

    public int getGornjaGranica() {
        return gornjaGranica;
    }

    public void setGornjaGranica(int gornjaGranica) {
        this.gornjaGranica = gornjaGranica;
    }
}
