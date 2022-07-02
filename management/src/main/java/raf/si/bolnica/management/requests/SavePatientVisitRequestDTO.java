package raf.si.bolnica.management.requests;

import lombok.Data;

import java.io.Serializable;

@Data
public class SavePatientVisitRequestDTO implements Serializable {

    private String lbp;

    private String patientName;

    private String patientSurname;

    private String patientPID;

    private String note;
}
