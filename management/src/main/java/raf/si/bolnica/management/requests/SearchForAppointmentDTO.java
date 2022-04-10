package raf.si.bolnica.management.requests;

import java.sql.Timestamp;
import java.util.UUID;

public class SearchForAppointmentDTO {

    private UUID lbz;
    private Timestamp date;

    public UUID getLbz() {
        return lbz;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setLbz(UUID lbz) {
        this.lbz = lbz;
    }
}
