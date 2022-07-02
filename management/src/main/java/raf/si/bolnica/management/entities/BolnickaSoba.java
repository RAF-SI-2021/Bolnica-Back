package raf.si.bolnica.management.entities;

import javax.persistence.*;

@Entity
public class BolnickaSoba {

    @Id
    @Column(name = "bolnicka_soba_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bolnickaSobaId;

    @Column(nullable = false)
    private long odeljenjeId;

    @Column(nullable = false)
    private int brojSobe;

    @Column(nullable = false)
    private int bolnicaId;

    private String nazivSobe;

    @Column(nullable = false)
    private int kapacitet;

    @Column(nullable = false)
    private int popunjenost = 0;

    private String opis;


    public long getBolnickaSobaId() {
        return bolnickaSobaId;
    }

    public void setBolnickaSobaId(long bolnickaSobaId) {
        this.bolnickaSobaId = bolnickaSobaId;
    }

    public long getOdeljenjeId() {
        return odeljenjeId;
    }

    public void setOdeljenjeId(long odeljenjeId) {
        this.odeljenjeId = odeljenjeId;
    }

    public int getBrojSobe() {
        return brojSobe;
    }

    public void setBrojSobe(int brojSobe) {
        this.brojSobe = brojSobe;
    }

    public String getNazivSobe() {
        return nazivSobe;
    }

    public void setNazivSobe(String nazivSobe) {
        this.nazivSobe = nazivSobe;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(int kapacitet) {
        this.kapacitet = kapacitet;
    }

    public int getPopunjenost() {
        return popunjenost;
    }

    public void setPopunjenost(int popunjenost) {
        this.popunjenost = popunjenost;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public int getBolnicaId() {
        return bolnicaId;
    }

    public void setBolnicaId(int bolnicaId) {
        this.bolnicaId = bolnicaId;
    }
}
