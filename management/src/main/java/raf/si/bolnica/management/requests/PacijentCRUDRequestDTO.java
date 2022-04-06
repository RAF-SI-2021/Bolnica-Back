package raf.si.bolnica.management.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.enums.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacijentCRUDRequestDTO {

    private String jmbg;

    private String ime;

    private String imeRoditelja;

    private String prezime;

    private Pol pol;

    private Date datumRodjenja;

    private Timestamp datumVremeSmrti;

    private String mestoRodjenja;

    private CountryCode zemljaDrzavljanstva;

    private String adresa;

    private String mestoStanovanja;

    private CountryCode zemljaStanovanja;

    private String kontaktTelefon;

    private String email;

    private String jmbgStaratelj;

    private String imeStaratelj;

    private PorodicniStatus porodicniStatus;

    private BracniStatus bracniStatus;

    private Integer brojDece;

    private StrucnaSprema stepenStrucneSpreme;

    private String zanimanje;
}
