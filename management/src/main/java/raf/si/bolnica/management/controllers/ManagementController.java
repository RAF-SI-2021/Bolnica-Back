package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.*;
import raf.si.bolnica.management.response.*;
import raf.si.bolnica.management.services.*;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

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

            return ok(new PacijentResponseDTO(kreiranPacijent));
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

            return ok(new PacijentResponseDTO(azuriranPacijent));
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
                alergenZdravstveniKartonService.save(azk);
            }

            s = "SELECT az FROM Vakcinacija az WHERE az.zdravstveniKarton = :zk";

            TypedQuery<Vakcinacija> query2 = entityManager.createQuery(s, Vakcinacija.class);

            query2.setParameter("zk",zdravstveniKarton);

            for(Vakcinacija v: query2.getResultList()) {
                v.setObrisan(true);
                vakcinacijaService.save(v);
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

    @GetMapping("/fetch-patient/{lbp}")
    public ResponseEntity<?> fetchPatientLbp(@PathVariable UUID lbp) {
        if(loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA) ||
                loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(lbp);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ok(new PacijentResponseDTO(pacijent));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-zdravstveni-karton/{lbp}")
    public ResponseEntity<?> fetchZdravstveniKartonLbp(@PathVariable UUID lbp) {
        if(loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(lbp);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ok(new ZdravstveniKartonResponseDTO(pacijent.getZdravstveniKarton()));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-patient-data/{lbp}")
    public ResponseEntity<?> fetchPatientDataLbp(@PathVariable UUID lbp) {
        if(loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(lbp);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ZdravstveniKarton zk = pacijent.getZdravstveniKarton();



            String vakcineUpitString = "SELECT az FROM Vakcinacija az WHERE az.zdravstveniKarton = :zk";

            Set<Vakcina> vakcine = new HashSet<>();

            TypedQuery<Vakcinacija> upitVakcine = entityManager.createQuery(vakcineUpitString, Vakcinacija.class);

            upitVakcine.setParameter("zk",zk);

            for(Vakcinacija v: upitVakcine.getResultList()) {
                vakcine.add(v.getVakcina());
            }


            String alergijeUpitString = "SELECT az FROM AlergenZdravstveniKarton az WHERE az.zdravstveniKarton = :zk";

            Set<Alergen> alergeni = new HashSet<>();

            TypedQuery<AlergenZdravstveniKarton> upitAlergeni = entityManager.createQuery(alergijeUpitString, AlergenZdravstveniKarton.class);

            upitAlergeni.setParameter("zk",zk);

            for(AlergenZdravstveniKarton az: upitAlergeni.getResultList()) {
                alergeni.add(az.getAlergen());
            }


            PacijentPodaciResponseDTO pacijentPodaciResponseDTO = new PacijentPodaciResponseDTO();

            pacijentPodaciResponseDTO.setRhFaktor(zk.getRhFaktor());

            pacijentPodaciResponseDTO.setKrvnaGrupa(zk.getKrvnaGrupa());

            pacijentPodaciResponseDTO.setAlergeni(alergeni);

            pacijentPodaciResponseDTO.setVakcine(vakcine);


            return ok(pacijentPodaciResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-pregledi/{lbp}")
    public ResponseEntity<?> fetchPreglediLbp(@RequestBody PreglediRequestDTO preglediRequestDTO,
                                                     @PathVariable UUID lbp,
                                                     @RequestParam int page,
                                                     @RequestParam int size) {
        if(loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(lbp);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ZdravstveniKarton zk = pacijent.getZdravstveniKarton();



            String preglediUpitString = "SELECT i FROM Pregled p WHERE p.zdravstveniKarton = :zk";

            if(!loggedInUser.getRoles().contains(Constants.SPECIJLISTA_POV)) {
                preglediUpitString = preglediUpitString + " AND p.indikatorPoverljivosti = false";
            }

            if(preglediRequestDTO.getOn() != null) {
                preglediUpitString = preglediUpitString + " AND p.datumPregelda = :on";
            }

            if(preglediRequestDTO.getFrom() != null) {
                preglediUpitString = preglediUpitString + " AND p.datumPregelda >= :from";
            }

            if(preglediRequestDTO.getTo() != null) {
                preglediUpitString = preglediUpitString + " AND p.datumPregelda <= :to";
            }

            List<PregledResponseDTO> pregledi = new ArrayList<>();

            TypedQuery<Pregled> upitPregledi = entityManager.createQuery(preglediUpitString, Pregled.class);

            upitPregledi.setParameter("zk",zk);

            if(preglediRequestDTO.getOn() != null) {
                upitPregledi.setParameter("on",preglediRequestDTO.getOn());
            }

            if(preglediRequestDTO.getFrom() != null) {
                upitPregledi.setParameter("from",preglediRequestDTO.getFrom());
            }

            if(preglediRequestDTO.getTo() != null) {
                upitPregledi.setParameter("to",preglediRequestDTO.getTo());
            }

            upitPregledi.setFirstResult((page-1)*size);
            upitPregledi.setMaxResults(size);

            for(Pregled p: upitPregledi.getResultList()) {
                pregledi.add(new PregledResponseDTO(p));
            }

            return ok(pregledi);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/fetch-istorija-bolesti/{lbp}")
    public ResponseEntity<?> fetchIstorijaBolestiLbp(@RequestBody IstorijaBolestiRequestDTO istorijaBolestiRequestDTO,
                                                     @PathVariable UUID lbp,
                                                     @RequestParam int page,
                                                     @RequestParam int size) {
        if(loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA)) {

            Pacijent pacijent = pacijentService.fetchPacijentByLbp(lbp);

            if(pacijent == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ZdravstveniKarton zk = pacijent.getZdravstveniKarton();



            String istorijaUpitString = "SELECT i FROM IstorijaBolesti i WHERE i.zdravstveniKarton = :zk";

            if(!loggedInUser.getRoles().contains(Constants.SPECIJLISTA_POV)) {
                istorijaUpitString = istorijaUpitString + " AND i.indikatorPoverljivosti = false";
            }

            if(istorijaBolestiRequestDTO.getDijagnoza() != null) {
                istorijaUpitString = istorijaUpitString + " AND i.dijagnoza like :dijagnoza";
            }

            List<IstorijaBolestiResponseDTO> istorija = new ArrayList<>();

            TypedQuery<IstorijaBolesti> upitIstorija = entityManager.createQuery(istorijaUpitString, IstorijaBolesti.class);

            upitIstorija.setParameter("zk",zk);

            if(istorijaBolestiRequestDTO.getDijagnoza() != null) {
                upitIstorija.setParameter("dijagnoza",istorijaBolestiRequestDTO.getDijagnoza());
            }
            upitIstorija.setFirstResult((page-1)*size);
            upitIstorija.setMaxResults(size);

            for(IstorijaBolesti i:upitIstorija.getResultList()) {
                istorija.add(new IstorijaBolestiResponseDTO(i));
            }

            return ok(istorija);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @GetMapping("/filter-patients")
    public ResponseEntity<?> filterPatients(@RequestBody FilterPatientsRequestDTO filterPatientsRequestDTO) {
        if(loggedInUser.getRoles().contains(Constants.NACELNIK) ||
                loggedInUser.getRoles().contains(Constants.SPECIJALISTA) ||
                loggedInUser.getRoles().contains(Constants.VISA_MED_SESTRA) ||
                loggedInUser.getRoles().contains(Constants.MED_SESTRA)) {

            String pacijentUpitString = "SELECT p FROM Pacijent p";

            int cnt = 0;

            if(filterPatientsRequestDTO.getLbp()!=null) {
                pacijentUpitString = pacijentUpitString + " WHERE p.lbp = :lbp";
                cnt = cnt + 1;
            }

            if(filterPatientsRequestDTO.getJmbg()!=null) {
                if(cnt==0) {
                    pacijentUpitString = pacijentUpitString + " WHERE ";
                }
                else {
                    pacijentUpitString = pacijentUpitString + " AND ";
                }
                pacijentUpitString = pacijentUpitString + "p.jmbg = :jmbg";
                cnt = cnt + 1;
            }

            if(filterPatientsRequestDTO.getIme()!=null) {
                if(cnt==0) {
                    pacijentUpitString = pacijentUpitString + " WHERE ";
                }
                else {
                    pacijentUpitString = pacijentUpitString + " AND ";
                }
                pacijentUpitString = pacijentUpitString + "p.ime = :ime";
                cnt = cnt + 1;
            }

            if(filterPatientsRequestDTO.getPrezime()!=null) {
                if(cnt==0) {
                    pacijentUpitString = pacijentUpitString + " WHERE ";
                }
                else {
                    pacijentUpitString = pacijentUpitString + " AND ";
                }
                pacijentUpitString = pacijentUpitString + "p.prezime = :prezime";
            }

            List<PacijentResponseDTO> pacijenti = new ArrayList<>();

            TypedQuery<Pacijent> upitPacijent =
                    entityManager.createQuery(pacijentUpitString, Pacijent.class);

            if(filterPatientsRequestDTO.getLbp()!=null) {
                upitPacijent.setParameter("lbp",filterPatientsRequestDTO.getLbp());
            }

            if(filterPatientsRequestDTO.getJmbg()!=null) {
                upitPacijent.setParameter("jmbg",filterPatientsRequestDTO.getJmbg());
            }

            if(filterPatientsRequestDTO.getIme()!=null) {
                upitPacijent.setParameter("ime",filterPatientsRequestDTO.getIme());
            }

            if(filterPatientsRequestDTO.getPrezime()!=null) {
                upitPacijent.setParameter("prezime",filterPatientsRequestDTO.getPrezime());
            }


            for(Pacijent p: upitPacijent.getResultList()) {
                pacijenti.add(new PacijentResponseDTO(p));
            }

            return ok(pacijenti);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
