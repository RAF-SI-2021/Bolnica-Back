package raf.si.bolnica.laboratory.requests;

import java.sql.Date;

public class FindScheduledLabExaminationsDTO {
    private String lbp;
    private Date date;

    public String getLbp() {
        return lbp;
    }

    public void setLbp(String lbp) {
        this.lbp = lbp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
