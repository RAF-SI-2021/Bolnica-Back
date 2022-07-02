package raf.si.bolnica.laboratory.entities;

public class LaboratoriskiNalazParametar {

    private long idParametra;

    private String parametar;

    private String jedinica_mere;

    private String ocitana_vrednost;

    private double min_ref_vrednost;

    private double max_ref_vrednost;

    public LaboratoriskiNalazParametar() {
    }

    public LaboratoriskiNalazParametar(int idParametra, String parametar, String jedinica_mere, String ocitana_vrednost, double min_ref_vrednost, double max_ref_vrednost) {
        this.idParametra = idParametra;
        this.parametar = parametar;
        this.jedinica_mere = jedinica_mere;
        this.ocitana_vrednost = ocitana_vrednost;
        this.min_ref_vrednost = min_ref_vrednost;
        this.max_ref_vrednost = max_ref_vrednost;
    }

    public long getIdParametra() {
        return idParametra;
    }

    public void setIdParametra(long idParametra) {
        this.idParametra = idParametra;
    }

    public String getParametar() {
        return parametar;
    }

    public void setParametar(String parametar) {
        this.parametar = parametar;
    }

    public String getJedinica_mere() {
        return jedinica_mere;
    }

    public void setJedinica_mere(String jedinica_mere) {
        this.jedinica_mere = jedinica_mere;
    }

    public String getOcitana_vrednost() {
        return ocitana_vrednost;
    }

    public void setOcitana_vrednost(String ocitana_vrednost) {
        this.ocitana_vrednost = ocitana_vrednost;
    }

    public double getMin_ref_vrednost() {
        return min_ref_vrednost;
    }

    public void setMin_ref_vrednost(double min_ref_vrednost) {
        this.min_ref_vrednost = min_ref_vrednost;
    }

    public double getMax_ref_vrednost() {
        return max_ref_vrednost;
    }

    public void setMax_ref_vrednost(double max_ref_vrednost) {
        this.max_ref_vrednost = max_ref_vrednost;
    }
}
