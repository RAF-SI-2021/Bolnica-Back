package raf.si.bolnica.management.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.enums.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacijentCRUDResponseDTO {

    private UUID lbp;

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

    public PacijentCRUDResponseDTO(Pacijent pacijent) {
        lbp = pacijent.getLbp();
        adresa = pacijent.getAdresa();
        bracniStatus = pacijent.getBracniStatus();
        brojDece = pacijent.getBrojDece();
        datumRodjenja = pacijent.getDatumRodjenja();
        email = pacijent.getEmail();
        datumVremeSmrti = pacijent.getDatumVremeSmrti();
        ime = pacijent.getIme();
        imeRoditelja = pacijent.getImeRoditelja();
        prezime = pacijent.getPrezime();
        imeStaratelj = pacijent.getImeStaratelj();
        jmbg = pacijent.getJmbg();
        jmbgStaratelj = pacijent.getJmbgStaratelj();
        kontaktTelefon = pacijent.getKontaktTelefon();
        zemljaStanovanja = pacijent.getZemljaStanovanja();
        zemljaDrzavljanstva = pacijent.getZemljaDrzavljanstva();
        zanimanje = pacijent.getZanimanje();
        stepenStrucneSpreme = pacijent.getStepenStrucneSpreme();
        porodicniStatus = pacijent.getPorodicniStatus();
        pol = pacijent.getPol();
        mestoStanovanja = pacijent.getMestoStanovanja();
        mestoRodjenja = pacijent.getMestoRodjenja();
    }
}
