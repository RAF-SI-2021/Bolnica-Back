package raf.si.bolnica.management.requests;

import java.sql.Timestamp;

public class SearchForAppointmentDTO {

    private long lbz;
    private Timestamp date;

    public Long getLbz() {
        return lbz;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setLbz(Long lbz) {
        this.lbz = lbz;
    }
}
