package raf.si.bolnica.management.requests;

import lombok.Data;

@Data
public class SavePatientVisitRequestDTO {

    private String lbp;

    private String patientName;

    private String patientSurname;

    private String patientPID;

    private String note;
}
