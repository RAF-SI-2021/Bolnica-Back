package raf.si.bolnica.management.requests;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SearchPatientsInHospitalDTO {

    private UUID lbp;
    private String name;
    private String surname;
    private String jmbg;
    private int pbb;

}
