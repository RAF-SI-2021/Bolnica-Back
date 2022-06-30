package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class SetPatientsStateDTO {

    private UUID lbpPacijenta;
    private UUID lbzRegistratora;
    private Timestamp datumVreme;

    private String temperatura;
    private String krvniPritisak;
    private String puls;
    private String primenjeneTerapije;
    private String opis;
}


