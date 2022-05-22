package raf.si.bolnica.laboratory.requests;

import java.sql.Date;
import java.util.UUID;

public class FindScheduledLabExaminationsDTO {
    private UUID lbp;
    private Date date;

    public UUID getLbp() {
        return lbp;
    }

    public void setLbp(UUID lbp) {
        this.lbp = lbp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
