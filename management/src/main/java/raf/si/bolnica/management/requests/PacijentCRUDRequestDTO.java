package raf.si.bolnica.management.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.enums.*;
import raf.si.bolnica.management.response.PacijentCRUDResponseDTO;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

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

    public void updatePacijentWithData(Pacijent pacijent) {
        pacijent.setAdresa(this.getAdresa());
        pacijent.setBracniStatus(this.getBracniStatus());
        pacijent.setBrojDece(this.getBrojDece());
        pacijent.setDatumRodjenja(this.getDatumRodjenja());
        pacijent.setEmail(this.getEmail());
        pacijent.setDatumVremeSmrti(this.getDatumVremeSmrti());
        pacijent.setIme(this.getIme());
        pacijent.setImeRoditelja(this.getImeRoditelja());
        pacijent.setPrezime(this.getPrezime());
        pacijent.setImeStaratelj(this.getImeStaratelj());
        pacijent.setJmbg(this.getJmbg());
        pacijent.setJmbgStaratelj(this.getJmbgStaratelj());
        pacijent.setKontaktTelefon(this.getKontaktTelefon());
        pacijent.setZemljaStanovanja(this.getZemljaStanovanja());
        pacijent.setZemljaDrzavljanstva(this.getZemljaDrzavljanstva());
        pacijent.setZanimanje(this.getZanimanje());
        pacijent.setStepenStrucneSpreme(this.getStepenStrucneSpreme());
        pacijent.setPorodicniStatus(this.getPorodicniStatus());
        pacijent.setPol(this.getPol());
        pacijent.setMestoStanovanja(this.getMestoStanovanja());
        pacijent.setMestoRodjenja(this.getMestoRodjenja());

    }
}
