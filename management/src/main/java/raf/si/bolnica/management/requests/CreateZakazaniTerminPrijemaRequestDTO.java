package raf.si.bolnica.management.requests;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class CreateZakazaniTerminPrijemaRequestDTO {

    private UUID lbp;
    private Timestamp datumVremePrijema;
    private String napomena;

}
