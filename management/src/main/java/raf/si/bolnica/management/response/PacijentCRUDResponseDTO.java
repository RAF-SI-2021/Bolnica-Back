package raf.si.bolnica.management.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raf.si.bolnica.management.dto.PacijentCRUDDTO;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.entities.enums.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class PacijentCRUDResponseDTO extends PacijentCRUDDTO {

    private UUID lbp;

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
