package raf.si.bolnica.laboratory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.laboratory.constants.Constants;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogRequestDTO;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogSearchRequestDTO;
import raf.si.bolnica.laboratory.dto.response.*;
import raf.si.bolnica.laboratory.dto.request.RezultatParametraAnalizeSaveRequestDTO;
import raf.si.bolnica.laboratory.entities.*;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;
import raf.si.bolnica.laboratory.entities.enums.StatusUputa;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.services.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class LaboratoryController {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private ZakazanLaboratorijskiPregledService laboratorijskiPregledService;

    @Autowired
    private UputService uputService;

    @Autowired
    private LaboratorijskiRadniNalogService radniNalogService;

    @Autowired
    private LaboratorijskaAnalizaService laboratorijskaAnalizaService;

    @Autowired
    private ParametarService parametarService;

    @Autowired
    private ParametarAnalizeService parametarAnalizeService;

    @Autowired
    private RezultatParametraAnalizeService rezultatParametraAnalizeService;

    @Autowired
    private EntityManager entityManager;

    /*
    TEST:
    void addUput(long uputId) {
        Uput uput = new Uput();
        uput.setUputId(uputId);
        uput.setLbp(UUID.randomUUID());
        uput.setZahtevaneAnalize("KKS,GLU,SE");
        uput.setLbz(UUID.randomUUID());
        uput.setDatumVremeKreiranja(Timestamp.valueOf(LocalDateTime.now()));
        uput.setIzOdeljenjaId(1);
        uput.setZaOdeljenjeId(1);
        uput.setTip(TipUputa.LABORATORIJA);
        uputService.saveUput(uput);
    }*/

    @PostMapping(value = "/create-laboratory-work-order")
    public ResponseEntity<?> createLaboratorijskiRadniNalog(@RequestParam long uputId) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LAB_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LAB_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        /*TEST:
        addUput(uputId);
        */

        Uput uput = uputService.fetchUputById(uputId);


        if(uput == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ne postoji uput sa prosledjenim id-em " + uputId);
        }

        UUID lbz = loggedInUser.getLBZ();

        UUID lbp = uput.getLbp();

        LaboratorijskiRadniNalog laboratorijskiRadniNalog = new LaboratorijskiRadniNalog();

        laboratorijskiRadniNalog.setUput(uput);

        laboratorijskiRadniNalog.setLbp(lbp);

        laboratorijskiRadniNalog.setDatumVremeKreiranja(Timestamp.valueOf(LocalDateTime.now()));

        laboratorijskiRadniNalog.setLbzTehnicar(lbz);

        /*TEST:
        laboratorijskiRadniNalog.setStatusObrade(StatusObrade.OBRADJEN);
         */

        laboratorijskiRadniNalog = radniNalogService.saveRadniNalog(laboratorijskiRadniNalog);

        String zahtevaneAnalize = uput.getZahtevaneAnalize();

        for(String skracenicaAnalize:zahtevaneAnalize.split(",")) {
            List<LaboratorijskaAnaliza> analize = laboratorijskaAnalizaService.getLaboratorijskaAnalizaBySkracenica(skracenicaAnalize);
            for(LaboratorijskaAnaliza analiza:analize) {
                List<ParametarAnalize> parametriAnalize = parametarAnalizeService.getParametarAnalizeByLaboratorijskaAnaliza(analiza);
                for(ParametarAnalize parametarAnalize:parametriAnalize) {
                    RezultatParametraAnalize rezultat = new RezultatParametraAnalize();
                    rezultat.setLaboratorijskiRadniNalog(laboratorijskiRadniNalog);
                    rezultat.setParametarAnalize(parametarAnalize);
                    rezultatParametraAnalizeService.saveRezultatParametraAnalize(rezultat);
                }
            }
        }

        return  ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(value = "/laboratory-work-order-history")
    public ResponseEntity<?> getLaboratorijskiRadniNalogIstorija(@RequestBody LaboratorijskiRadniNalogRequestDTO request,
                                                         @RequestParam int page,
                                                         @RequestParam int size) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE (l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.U_OBRADI OR l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN)";

        Map<String,Object> param = new HashMap<>();

        if(request.getLbp()!=null) {
            s = s + " AND l.lbp = :lbp";
            param.put("lbp",request.getLbp());
        }

        if(request.getOdDatuma()!=null) {
            s = s + " AND l.datumVremeKreiranja >= :od";
            param.put("od",request.getOdDatuma());
        }

        if(request.getDoDatuma()!=null) {
            s = s + " AND l.datumVremeKreiranja <= :do";
            param.put("do",request.getDoDatuma());
        }

        TypedQuery<LaboratorijskiRadniNalog> query
                = entityManager.createQuery(
                s, LaboratorijskiRadniNalog.class);
        for (String t : param.keySet()) {
            query.setParameter(t, param.get(t));
        }

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        List<LaboratorijskiRadniNalogResponseDTO> ret = new ArrayList<>();

        for(LaboratorijskiRadniNalog nalog:query.getResultList()) {
            ret.add(new LaboratorijskiRadniNalogResponseDTO(nalog));
        }

        return  ok(ret);
    }

    @GetMapping(value = "/fetch-analysis-results")
    public ResponseEntity<?> fetchRezultatiParametaraAnalize(@RequestParam long id) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);
        acceptedRoles.add(Constants.LAB_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LAB_TEHNICAR);
        acceptedRoles.add(Constants.BIOHEMICAR);
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE l.laboratorijskiRadniNalogId = :id";
        Map<String,Object> param = new HashMap<>();
        param.put("id",id);
        acceptedRoles.clear();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);
        acceptedRoles.add(Constants.LAB_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LAB_TEHNICAR);
        String msg = "";
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            s = s + " AND l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN";
            msg = "koji je obradjen ";
        }

        TypedQuery<LaboratorijskiRadniNalog> queryNalog
                = entityManager.createQuery(
                s, LaboratorijskiRadniNalog.class);
        for (String t : param.keySet()) {
            queryNalog.setParameter(t, param.get(t));
        }

        List<LaboratorijskiRadniNalog> nalozi = queryNalog.getResultList();

        if(nalozi.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ne postoji nalog " + msg + "sa id-em " + id);
        }

        LaboratorijskiRadniNalog nalog = nalozi.get(0);

        param.clear();

        param.put("nalog",nalog);

        s = "SELECT r from RezultatParametraAnalize r WHERE r.laboratorijskiRadniNalog = :nalog";

        TypedQuery<RezultatParametraAnalize> queryRezultati
                = entityManager.createQuery(
                s, RezultatParametraAnalize.class);
        for (String t : param.keySet()) {
            queryRezultati.setParameter(t, param.get(t));
        }

        RezultatiParametaraAnaliizeResponseDTO rezultati = new RezultatiParametaraAnaliizeResponseDTO();

        rezultati.setNalog(new LaboratorijskiRadniNalogResponseDTO(nalog));

        List<ParametarSaAnalizomResponseDTO> parametriSaAnalizom = new ArrayList<>();

        for(RezultatParametraAnalize rezultat:queryRezultati.getResultList()) {
            ParametarAnalize parametarAnalize = rezultat.getParametarAnalize();
            Parametar parametar = parametarAnalize.getParametar();
            LaboratorijskaAnaliza analiza = parametarAnalize.getLaboratorijskaAnaliza();
            parametriSaAnalizom.add(new ParametarSaAnalizomResponseDTO(new RezultatParametraAnalizeResponseDTO(rezultat),new ParametarResponseDTO(parametar),new LaboratorijskaAnalizaResponseDTO(analiza)));
        }

        rezultati.setRezultatiAnaliza(parametriSaAnalizom);

        return ok(rezultati);
    }

    @PutMapping(value = "/save-analysis-result")
    public ResponseEntity<?> saveRezultatParametaraAnalize(@RequestBody RezultatParametraAnalizeSaveRequestDTO request) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.BIOHEMICAR);
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LaboratorijskiRadniNalog nalog = radniNalogService.fetchRadniNalogById(request.getNalogId());
        if(nalog.getStatusObrade() == StatusObrade.NEOBRADJEN) {
            nalog.setStatusObrade(StatusObrade.U_OBRADI);
            radniNalogService.saveRadniNalog(nalog);
        }

        RezultatParametraAnalizeKey id = new RezultatParametraAnalizeKey();
        id.setLaboratorijskiRadniNalogId(request.getNalogId());
        id.setParametarAnalizeId(request.getParametarId());

        RezultatParametraAnalize rezultatParametraAnalize = rezultatParametraAnalizeService.getRezultatParametraAnalize(id);

        if(rezultatParametraAnalize == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ne postoji rezultat kojem bi trebalo azurirati podatke");
        }

        rezultatParametraAnalize.setRezultat(request.getRezultat());

        rezultatParametraAnalize.setLbz(loggedInUser.getLBZ());

        rezultatParametraAnalize.setDatumVreme(Timestamp.valueOf(LocalDateTime.now()));

        rezultatParametraAnalizeService.saveRezultatParametraAnalize(rezultatParametraAnalize);


        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping(value = "/fetch-laboratory-work-orders")
    public ResponseEntity<?> getLaboratorijskiRadniNalogPretraga(@RequestBody LaboratorijskiRadniNalogSearchRequestDTO request,
                                                                 @RequestParam int page,
                                                                 @RequestParam int size) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.BIOHEMICAR);
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        acceptedRoles.add(Constants.LAB_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LAB_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String s = "SELECT lrn FROM LaboratorijskiRadniNalog lrn INNER JOIN lrn.uput u WHERE u.zaOdeljenjeId = :lab";
        Map<String,Object> param = new HashMap<>();
        param.put("lab",loggedInUser.getOdeljenjeId());

        if(request.getLbp()!=null) {
            s = s  + " AND lrn.lbp = :lbp";
            param.put("lbp",request.getLbp());
        }

        if(request.getStatusObrade()!=null) {
            s = s + " AND lrn.statusObrade = :status";
            param.put("status",request.getStatusObrade());
        }

        if(request.getOdDatuma()!=null) {
            s = s + " AND lrn.datumVremeKreiranja >= :od";
            param.put("od",request.getOdDatuma());
        }

        if(request.getDoDatuma()!=null) {
            s = s + " AND lrn.datumVremeKreiranja <= :do";
            param.put("do",request.getDoDatuma());
        }

        TypedQuery<LaboratorijskiRadniNalog> query
                = entityManager.createQuery(
                s, LaboratorijskiRadniNalog.class);
        for (String t : param.keySet()) {
            query.setParameter(t, param.get(t));
        }

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        List<LaboratorijskiRadniNalogResponseDTO> ret = new ArrayList<>();

        for(LaboratorijskiRadniNalog nalog:query.getResultList()) {
            ret.add(new LaboratorijskiRadniNalogResponseDTO(nalog));
        }

        return  ok(ret);
    }

    @PutMapping(value = "/verify-work-order")
    public ResponseEntity<?> verifyLaboratorijskiRadniNalog(@RequestParam long id) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LaboratorijskiRadniNalog nalog = radniNalogService.fetchRadniNalogById(id);

        Map<String,Object> param = new HashMap<>();
        String s = "SELECT r from RezultatParametraAnalize r WHERE r.laboratorijskiRadniNalog = :nalog";
        param.put("nalog",nalog);

        TypedQuery<RezultatParametraAnalize> queryRezultati
                = entityManager.createQuery(
                s, RezultatParametraAnalize.class);
        for (String t : param.keySet()) {
            queryRezultati.setParameter(t, param.get(t));
        }

        boolean hasResults = true;
        String nazivAnalize = "";


        for(RezultatParametraAnalize rezultat:queryRezultati.getResultList()) {
            if(rezultat.getRezultat()==null) {
                nazivAnalize = rezultat.getParametarAnalize().getLaboratorijskaAnaliza().getNazivAnalize();
                hasResults = false;
            }
        }

        if(!hasResults) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Nije moguce verifikovati nalog sa id-em " + id + " jer nisu upisani rezultati za analizu '" + nazivAnalize + "'");
        }

        Uput u = nalog.getUput();

        u.setStatus(StatusUputa.REALIZOVAN);

        uputService.saveUput(u);

        nalog.setStatusObrade(StatusObrade.OBRADJEN);

        nalog.setLbzBiohemicar(loggedInUser.getLBZ());

        radniNalogService.saveRadniNalog(nalog);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
