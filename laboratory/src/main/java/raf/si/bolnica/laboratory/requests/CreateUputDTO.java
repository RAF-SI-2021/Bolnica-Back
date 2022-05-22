package raf.si.bolnica.laboratory.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUputDTO {
    //neophodni parametri
    private TipUputa tip;
    private UUID lbz;
    private Integer izOdeljenjaId;
    private Integer zaOdeljenjeId;
    private UUID lbp;
    private Timestamp datumVremeKreiranja;


    //opcioni parametri
    private String zahtevaneAnalize;
    private String komentar;
    private String uputnaDijagnoza;
    private String razlogUpucivanja;

}
