package raf.si.bolnica.management.requests;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FilterPatientsRequestDTO {
    private String jmbg;
    private UUID lbp;
    private String ime;
    private String prezime;
}
