package raf.si.bolnica.laboratory.requests;

import java.sql.Date;
import java.util.UUID;

public class ScheduleLabExaminationDTO {

    private UUID lbp;
    private Date date;
    private String napomena;

    public UUID getLbp() {
        return lbp;
    }

    public Date getDate() {
        return date;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setLbp(UUID lbp) {
        this.lbp = lbp;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
