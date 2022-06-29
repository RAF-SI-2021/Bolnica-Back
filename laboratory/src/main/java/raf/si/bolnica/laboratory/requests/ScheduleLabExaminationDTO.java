package raf.si.bolnica.laboratory.requests;

import java.sql.Date;

public class ScheduleLabExaminationDTO {

    private String lbp;
    private Date date;
    private String napomena;

    public String getLbp() {
        return lbp;
    }

    public Date getDate() {
        return date;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setLbp(String lbp) {
        this.lbp = lbp;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
