package raf.si.bolnica.laboratory.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUputDTO {
    //neophodni parametri
    private TipUputa tip;
    private String lbz;
    private Integer izOdeljenjaId;
    private Integer zaOdeljenjeId;
    private String lbp;
    private Timestamp datumVremeKreiranja;


    //opcioni parametri
    private String zahtevaneAnalize;
    private String komentar;
    private String uputnaDijagnoza;
    private String razlogUpucivanja;

}
