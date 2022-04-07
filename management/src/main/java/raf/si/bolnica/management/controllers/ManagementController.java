package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.PacijentCRUDRequestDTO;
import raf.si.bolnica.management.requests.PacijentCRUDRequestValidator;
import raf.si.bolnica.management.response.PacijentCRUDResponseDTO;
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
        if(loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA)) {

            String msg = PacijentCRUDRequestValidator.checkValid(request);

            if(!msg.equals("ok")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
            }

            Pacijent pacijent = new Pacijent();

            pacijent.setLbp(UUID.randomUUID());

            request.updatePacijentWithData(pacijent);

            ZdravstveniKarton zdravstveniKarton = new ZdravstveniKarton();

            zdravstveniKarton.setDatumRegistracije(Date.valueOf(LocalDate.now()));

            Pacijent kreiranPacijent = pacijentService.savePacijent(pacijent);

            zdravstveniKarton.setPacijent(kreiranPacijent);

            ZdravstveniKarton kreiranZdravstveniKarton = zdravstveniKartonService.saveZdravstveniKarton(zdravstveniKarton);

            kreiranPacijent.setZdravstveniKarton(kreiranZdravstveniKarton);

            return ok(new PacijentCRUDResponseDTO(kreiranPacijent));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/update-patient/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody PacijentCRUDRequestDTO request,@PathVariable Long id) {
        if(loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA)) {

            String msg = PacijentCRUDRequestValidator.checkValid(request);

            if(!msg.equals("ok")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
            }

            Pacijent pacijent = pacijentService.fetchPacijentById(id);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            request.updatePacijentWithData(pacijent);

            Pacijent azuriranPacijent = pacijentService.savePacijent(pacijent);

            return ok(new PacijentCRUDResponseDTO(azuriranPacijent));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/remove-patient/{id}")
    public ResponseEntity<?> removePatient(@PathVariable Long id) {
        if(loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA)) {

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

            query1.setParameter("zk",zdravstveniKarton);

            for(AlergenZdravstveniKarton azk : query1.getResultList()) {
                azk.setObrisan(true);
                alergenZdravstveniKartonService.saveAlergenZdravstveniKarton(azk);
            }

            s = "SELECT az FROM Vakcinacija az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Vakcinacija> query2 = entityManager.createQuery(s, Vakcinacija.class);

            query2.setParameter("zk",zdravstveniKarton);

            for(Vakcinacija v: query2.getResultList()) {
                v.setObrisan(true);
                vakcinacijaService.saveVakcinacija(v);
            }

            s = "SELECT az FROM Operacija az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Operacija> query3 = entityManager.createQuery(s, Operacija.class);

            query3.setParameter("zk",zdravstveniKarton);

            for(Operacija o: query3.getResultList()) {
                o.setObrisan(true);
                operacijaService.saveOperacija(o);
            }

            s = "SELECT az FROM Pregled az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Pregled> query4 = entityManager.createQuery(s, Pregled.class);

            query4.setParameter("zk",zdravstveniKarton);

            for(Pregled p: query4.getResultList()) {
                p.setObrisan(true);
                pregledService.savePregled(p);
            }

            s = "SELECT az FROM IstorijaBolesti az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<IstorijaBolesti> query5 = entityManager.createQuery(s, IstorijaBolesti.class);

            query5.setParameter("zk",zdravstveniKarton);

            for(IstorijaBolesti i: query5.getResultList()) {
                i.setObrisan(true);
                istorijaBolestiService.saveIstorijaBolesti(i);
            }

            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
