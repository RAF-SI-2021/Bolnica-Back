package raf.si.bolnica.management.requests;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class CreateScheduledAppointmentRequestDTO {


    //Employee (nurse) that makes appointment
    private long appointmentEmployeeId;

    private Timestamp dateAndTimeOfAppointment;

    private String note;

    //Employee (doctor) that will be conducting examination
    private long examinationEmployeeId;


    public long getAppointmentEmployeeId() {
        return appointmentEmployeeId;
    }

    public void setAppointmentEmployeeId(long appointmentEmployeeId) {
        this.appointmentEmployeeId = appointmentEmployeeId;
    }

    public Timestamp getDateAndTimeOfAppointment() {
        return dateAndTimeOfAppointment;
    }

    public void setDateAndTimeOfAppointment(Timestamp dateAndTimeOfAppointment) {
        this.dateAndTimeOfAppointment = dateAndTimeOfAppointment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getExaminationEmployeeId() {
        return examinationEmployeeId;
    }

    public void setExaminationEmployeeId(long examinationEmployeeId) {
        this.examinationEmployeeId = examinationEmployeeId;
    }
}




