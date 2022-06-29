package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class HospitalizePatientDTO {

    private long bolnickaSobaId;
    private UUID lbp;
    private UUID lbzLekara;

    private String napomena;
    private long uputId;
    private String uputnaDijagnoza;

}
