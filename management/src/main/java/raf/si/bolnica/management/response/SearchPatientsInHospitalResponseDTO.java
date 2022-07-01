package raf.si.bolnica.management.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class SearchPatientsInHospitalResponseDTO {

    //bolnicka soba
    private long bolnickaSobaId;
    private int brojSobe;

    //pacijent
    private UUID lbp;
    private String ime;
    private String prezime;
    private Date datumRodjenja;
    private String jmbg;

    //hospitalizacija
    private Timestamp datumVremePrijema;
    private UUID lbzDodeljenogLekara;
    private String uputnaDijagnoza;
    private String napomena;
    private int idOdeljenja;
    //  private String nazivOdeljenja; //uzeti iz usera
}
