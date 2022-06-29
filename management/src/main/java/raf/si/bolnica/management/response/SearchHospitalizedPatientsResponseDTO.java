package raf.si.bolnica.management.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchHospitalizedPatientsResponseDTO {

    //hospitalizacija
    private Timestamp datumVremePrijema;
    private UUID lbzDodeljenogLekara;

    private String uputnaDijagnoza;
    private String napomena;

    //bolnicka soba
    private long bolnickaSobaId;
    private int brojSobe;
    private int kapacitetSobe;

    //pacijent
    private UUID lbp;
    private String ime;
    private String prezime;
    private Date datumRodjenja;
    private String jmbg;


}
