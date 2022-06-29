package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SearchHospitalizedPatientsDTO {
    //Id odeljenja
    private long pbo;

    private UUID lbp;
    private String jmbg;
    private String name;
    private String surname;

}
