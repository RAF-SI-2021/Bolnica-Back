package raf.si.bolnica.management.requests;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

public class CreateScheduledAppointmentRequestDTO {

    private Timestamp dateAndTimeOfAppointment;

    private String note;

    //Employee (doctor) that will be conducting examination
    private UUID examinationEmployeeId;

    private UUID patient;

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

    public UUID getExaminationEmployeeId() {
        return examinationEmployeeId;
    }

    public void setExaminationEmployeeId(UUID examinationEmployeeId) {
        this.examinationEmployeeId = examinationEmployeeId;
    }

    public UUID getPatient() {
        return patient;
    }

    public void setPatient(UUID patient) {
        this.patient = patient;
    }
}




