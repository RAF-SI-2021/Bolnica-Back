package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.bolnica.management.entities.Pacijent;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.services.PacijentService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class ManagementController {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private PacijentService pacijentService;

    @PostMapping("/create-patient")
    public ResponseEntity<?> createPatient(@RequestBody PacijentCRUDRequestDTO request) {
        if(loggedInUser.getRoles().contains("ROLE_VISA_MED_SESTRA") ||
                loggedInUser.getRoles().contains("ROLE_MED_SESTRA")) {

            Pacijent pacijent = new Pacijent();

            pacijent.setAdresa(request.getAdresa());
            pacijent.setBracniStatus(request.getBracniStatus());
            pacijent.setBrojDece(request.getBrojDece());
            pacijent.setDatumRodjenja(request.getDatumRodjenja());
            pacijent.setEmail(request.getEmail());
            pacijent.setDatumVremeSmrti(request.getDatumVremeSmrti());
            pacijent.setIme(request.getIme());
            pacijent.setImeRoditelja(request.getImeRoditelja());
            pacijent.setPrezime(request.getPrezime());
            pacijent.setImeStaratelj(request.getImeStaratelj());
            pacijent.setJmbg(request.getJmbg());
            pacijent.setJmbgStaratelj(request.getJmbgStaratelj());
            pacijent.setKontaktTelefon(request.getKontaktTelefon());
            pacijent.setZemljaStanovanja(request.getZemljaStanovanja());
            pacijent.setZemljaDrzavljanstva(request.getZemljaDrzavljanstva());
            pacijent.setZanimanje(request.getZanimanje());
            pacijent.setStepenStrucneSpreme(request.getStepenStrucneSpreme());
            pacijent.setPorodicniStatus(request.getPorodicniStatus());
            pacijent.setPol(request.getPol());
            pacijent.setMestoStanovanja(request.getMestoStanovanja());
            pacijent.setMestoRodjenja(request.getMestoRodjenja());

            return ok(pacijent);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
