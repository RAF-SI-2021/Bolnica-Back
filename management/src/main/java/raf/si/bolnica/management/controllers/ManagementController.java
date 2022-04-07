package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.services.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class ManagementController {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private PacijentService pacijentService;

    @Autowired
    private ZdravstveniKartonService zdravstveniKartonService;

    @Autowired
    private AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Autowired
    private VakcinacijaService vakcinacijaService;

    @Autowired
    private OperacijaService operacijaService;

    @Autowired
    private PregledService pregledService;

    @Autowired
    private IstorijaBolestiService istorijaBolestiService;

    @Autowired
    private EntityManager entityManager;

    @PostMapping("/create-patient")
    public ResponseEntity<?> createPatient(@RequestBody PacijentCRUDRequestDTO request) {
        if(loggedInUser.getRoles().contains("ROLE_VISA_MED_SESTRA") ||
                loggedInUser.getRoles().contains("ROLE_MED_SESTRA")) {

            Pacijent pacijent = new Pacijent();

            pacijent.setLbp(UUID.randomUUID());
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

            ZdravstveniKarton zdravstveniKarton = new ZdravstveniKarton();

            zdravstveniKarton.setDatumRegistracije(Date.valueOf(LocalDate.now()));

            Pacijent kreiranPacijent = pacijentService.savePacijent(pacijent);

            zdravstveniKarton.setPacijent(kreiranPacijent);

            zdravstveniKartonService.saveZdravstveniKarton(zdravstveniKarton);

            return ok(kreiranPacijent);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/update-patient/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody PacijentCRUDRequestDTO request,@PathVariable Long id) {
        if(loggedInUser.getRoles().contains("ROLE_VISA_MED_SESTRA") ||
                loggedInUser.getRoles().contains("ROLE_MED_SESTRA")) {

            Pacijent pacijent = pacijentService.fetchPacijentById(id);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

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

            Pacijent azuriranPacijent = pacijentService.savePacijent(pacijent);

            return ok(azuriranPacijent);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/remove-patient/{id}")
    public ResponseEntity<?> removePatient(@PathVariable Long id) {
        if(loggedInUser.getRoles().contains("ROLE_VISA_MED_SESTRA")) {

            Pacijent pacijent = pacijentService.fetchPacijentById(id);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            pacijent.setObrisan(true);

            pacijentService.savePacijent(pacijent);

            ZdravstveniKarton zdravstveniKarton = pacijent.getZdravstveniKarton();

            zdravstveniKarton.setObrisan(true);

            zdravstveniKartonService.saveZdravstveniKarton(zdravstveniKarton);

            String s = "SELECT az FROM AlergenZdravstveniKarton az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<AlergenZdravstveniKarton> query1 = entityManager.createQuery(s, AlergenZdravstveniKarton.class);

            query1.setParameter("zk",zdravstveniKarton.getZdravstveniKartonId());

            for(AlergenZdravstveniKarton azk : query1.getResultList()) {
                azk.setObrisan(true);
                alergenZdravstveniKartonService.saveAlergenZdravstveniKarton(azk);
            }

            s = "SELECT az FROM Vakcinacija az WHERE az.zdravstveniKartonId = :zk";

            TypedQuery<Vakcinacija> query2 = entityManager.createQuery(s, Vakcinacija.class);

            query2.setParameter("zk",zdravstveniKarton.getZdravstveniKartonId());

            for(Vakcinacija v: query2.getResultList()) {
                v.setObrisan(true);
                vakcinacijaService.saveVakcinacija(v);
            }

            s = "SELECT az FROM Operacija az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Operacija> query3 = entityManager.createQuery(s, Operacija.class);

            query3.setParameter("zk",zdravstveniKarton.getZdravstveniKartonId());

            for(Operacija o: query3.getResultList()) {
                o.setObrisan(true);
                operacijaService.saveOperacija(o);
            }

            s = "SELECT az FROM Pregled az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Pregled> query4 = entityManager.createQuery(s, Pregled.class);

            query4.setParameter("zk",zdravstveniKarton.getZdravstveniKartonId());

            for(Pregled p: query4.getResultList()) {
                p.setObrisan(true);
                pregledService.savePregled(p);
            }

            s = "SELECT az FROM IstorijaBolesti az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<IstorijaBolesti> query5 = entityManager.createQuery(s, IstorijaBolesti.class);

            query5.setParameter("zk",zdravstveniKarton.getZdravstveniKartonId());

            for(IstorijaBolesti i: query5.getResultList()) {
                i.setObrisan(true);
                istorijaBolestiService.saveIstorijaBolesti(i);
            }

            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
