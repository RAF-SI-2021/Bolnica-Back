package raf.si.bolnica.laboratory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.laboratory.constants.Constants;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogRequestDTO;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogSearchRequestDTO;
import raf.si.bolnica.laboratory.dto.request.RezultatParametraAnalizeSaveRequestDTO;
import raf.si.bolnica.laboratory.dto.request.UputHistoryRequestDTO;
import raf.si.bolnica.laboratory.dto.response.*;
import raf.si.bolnica.laboratory.entities.*;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;
import raf.si.bolnica.laboratory.entities.enums.StatusPregleda;
import raf.si.bolnica.laboratory.entities.enums.StatusUputa;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.requests.*;
import raf.si.bolnica.laboratory.services.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
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
    private ZakazanLaboratorijskiPregledService zakazanLaboratorijskiPregledService;

    @Autowired
    private ParametarAnalizeService parametarAnalizeService;

    @Autowired
    private RezultatParametraAnalizeService rezultatParametraAnalizeService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @PostMapping(value = "/get-lab-examination-count")
    public ResponseEntity<Integer> getLabExaminationsOnDate(@RequestBody GetLabExaminationsByDateDTO getLabExaminationsByDateDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Date date = new Date(getLabExaminationsByDateDTO.getDateAndTime().getTime());

        return ResponseEntity.ok(zakazanLaboratorijskiPregledService.getZakazaniPreglediByDate(date).size());
    }

    @PostMapping(value = "/get-lab-examinations")
    public ResponseEntity<List<ZakazanLaboratorijskiPregled>> getLabExaminations(@RequestBody FindScheduledLabExaminationsDTO findScheduledLabExaminationsDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (findScheduledLabExaminationsDTO.getDate() != null && findScheduledLabExaminationsDTO.getLbp() != null) {
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndZakazanDatumAndLbp(loggedInUser.getOdeljenjeId(), findScheduledLabExaminationsDTO.getDate(), UUID.fromString(findScheduledLabExaminationsDTO.getLbp())));
        } else if (findScheduledLabExaminationsDTO.getDate() != null) {
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndZakazanDatum(loggedInUser.getOdeljenjeId(), findScheduledLabExaminationsDTO.getDate()));
        } else if (findScheduledLabExaminationsDTO.getLbp() != null) {
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndLbp(loggedInUser.getOdeljenjeId(), UUID.fromString(findScheduledLabExaminationsDTO.getLbp())));
        } else {
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeId(loggedInUser.getOdeljenjeId()));
        }
    }

    @PutMapping(value = "/set-lab-examination-status")
    public ResponseEntity<?> setLabExaminationStatus(@RequestBody SetStatusExaminationDTO request) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ZakazanLaboratorijskiPregled zakazanLaboratorijskiPregled = zakazanLaboratorijskiPregledService.getZakazanPregled(request.getId());

        zakazanLaboratorijskiPregled.setStatusPregleda(StatusPregleda.valueOf(request.getStatus()));
        zakazanLaboratorijskiPregledService.saveZakazanPregled(zakazanLaboratorijskiPregled);
        return ResponseEntity.ok(new ZakazanPregledDto(zakazanLaboratorijskiPregled));

    }

    @PostMapping(value = "/schedule-lab-examination")
    public ResponseEntity<ZakazanLaboratorijskiPregled> scheduleLabExamination(@RequestBody ScheduleLabExaminationDTO request) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        ZakazanLaboratorijskiPregled pregled = new ZakazanLaboratorijskiPregled();
        pregled.setStatusPregleda(StatusPregleda.ZAKAZANO);
        pregled.setLbp(UUID.fromString(request.getLbp()));
        pregled.setNapomena(request.getNapomena());
        pregled.setZakazanDatum(request.getDate());
        pregled.setLbz(loggedInUser.getLBZ());
        pregled.setOdeljenjeId(loggedInUser.getOdeljenjeId());
        zakazanLaboratorijskiPregledService.saveZakazanPregled(pregled);

        return ResponseEntity.ok(pregled);
    }

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
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        /*TEST:
        addUput(uputId);
        */

        Uput uput = uputService.fetchUputById(uputId);

        if (uput == null) {
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

        LaboratorijskiRadniNalog noviNalog = radniNalogService.saveRadniNalog(laboratorijskiRadniNalog);

        String zahtevaneAnalize = uput.getZahtevaneAnalize();

        for (String skracenicaAnalize : zahtevaneAnalize.split(",")) {
            List<LaboratorijskaAnaliza> analize = laboratorijskaAnalizaService.getLaboratorijskaAnalizaBySkracenica(skracenicaAnalize);
            for (LaboratorijskaAnaliza analiza : analize) {
                List<ParametarAnalize> parametriAnalize = parametarAnalizeService.getParametarAnalizeByLaboratorijskaAnaliza(analiza);
                for (ParametarAnalize parametarAnalize : parametriAnalize) {
                    RezultatParametraAnalize rezultat = new RezultatParametraAnalize();
                    rezultat.setLaboratorijskiRadniNalog(noviNalog);
                    rezultat.setParametarAnalize(parametarAnalize);
                    rezultatParametraAnalizeService.saveRezultatParametraAnalize(rezultat);
                }
            }
        }
        return ResponseEntity.ok(new LaboratorijskiRadniNalogResponseDTO(noviNalog));
    }

    @PostMapping(value = "/laboratory-work-order-history")
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

        Map<String, Object> param = new HashMap<>();

        if (request.getLbp() != null) {
            s = s + " AND l.lbp = :lbp";
            param.put("lbp", request.getLbp());
        }

        if (request.getOdDatuma() != null) {
            s = s + " AND l.datumVremeKreiranja >= :od";
            param.put("od", request.getOdDatuma());
        }

        if (request.getDoDatuma() != null) {
            s = s + " AND l.datumVremeKreiranja <= :do";
            param.put("do", request.getDoDatuma());
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

        for (LaboratorijskiRadniNalog nalog : query.getResultList()) {
            ret.add(new LaboratorijskiRadniNalogResponseDTO(nalog));
        }

        return ok(ret);
    }

    @GetMapping(value = "/fetch-analysis-results")
    public ResponseEntity<?> fetchRezultatiParametaraAnalize(@RequestParam long id) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.BIOHEMICAR);
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE l.laboratorijskiRadniNalogId = :id";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        acceptedRoles.clear();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        String msg = "";
//        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
//            s = s + " AND l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN";
//            msg = "koji je obradjen ";
//        }

        TypedQuery<LaboratorijskiRadniNalog> queryNalog = entityManager.createQuery(s, LaboratorijskiRadniNalog.class);
        for (String t : param.keySet()) {
            queryNalog.setParameter(t, param.get(t));
        }
        //keystone
        List<LaboratorijskiRadniNalog> nalozi = queryNalog.getResultList();

        if (nalozi.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ne postoji nalog " + msg + "sa id-em " + id);
        }

        LaboratorijskiRadniNalog nalog = nalozi.get(0);

        param.clear();

        param.put("nalog", nalog);

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

        for (RezultatParametraAnalize rezultat : queryRezultati.getResultList()) {
            ParametarAnalize parametarAnalize = rezultat.getParametarAnalize();
            Parametar parametar = parametarAnalize.getParametar();
            LaboratorijskaAnaliza analiza = parametarAnalize.getLaboratorijskaAnaliza();
            parametriSaAnalizom.add(new ParametarSaAnalizomResponseDTO(new RezultatParametraAnalizeResponseDTO(rezultat), new ParametarResponseDTO(parametar), new LaboratorijskaAnalizaResponseDTO(analiza)));
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
        if (nalog.getStatusObrade() == StatusObrade.NEOBRADJEN) {
            nalog.setStatusObrade(StatusObrade.U_OBRADI);
            radniNalogService.saveRadniNalog(nalog);
        }

        RezultatParametraAnalizeKey id = new RezultatParametraAnalizeKey();
        id.setLaboratorijskiRadniNalogId(request.getNalogId());
        id.setParametarAnalizeId(request.getParametarId());

        RezultatParametraAnalize rezultatParametraAnalize = rezultatParametraAnalizeService.getRezultatParametraAnalize(id);

        if (rezultatParametraAnalize == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ne postoji rezultat kojem bi trebalo azurirati podatke");
        }

        rezultatParametraAnalize.setRezultat(request.getRezultat());

        rezultatParametraAnalize.setLbz(loggedInUser.getLBZ());

        rezultatParametraAnalize.setDatumVreme(Timestamp.valueOf(LocalDateTime.now()));

        rezultatParametraAnalizeService.saveRezultatParametraAnalize(rezultatParametraAnalize);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping(value = "/fetch-laboratory-work-orders")
    public ResponseEntity<?> getLaboratorijskiRadniNalogPretraga(@RequestBody LaboratorijskiRadniNalogSearchRequestDTO request,
                                                                 @RequestParam int page,
                                                                 @RequestParam int size) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.BIOHEMICAR);
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

//        String s = "SELECT lrn FROM LaboratorijskiRadniNalog lrn INNER JOIN lrn.uput u WHERE u.zaOdeljenjeId = :lab";
        String s = "";
        s = "SELECT lrn FROM LaboratorijskiRadniNalog lrn";
        Map<String, Object> param = new HashMap<>();
//        param.put("lab", loggedInUser.getOdeljenjeId());
        boolean hasAnyParams = false;

        if (request.getLbp() != null) {
            hasAnyParams = true;
            param.put("lbp", request.getLbp());
            s = s + " WHERE lrn.lbp = :lbp";
        }

        if (request.getDoDatuma() != null) {
            if (!hasAnyParams) {
                hasAnyParams = true;
                s = s + " WHERE";
            } else {
                s = s + " AND";
            }
            param.put("do", request.getDoDatuma());
            s = s + " lrn.datumVremeKreiranja <= :do";
        }

        if (request.getStatusObrade() != null) {
            if (!hasAnyParams) {
                hasAnyParams = true;
                s = s + " WHERE";
            } else {
                s = s + " AND";
            }
            param.put("status", request.getStatusObrade());
            s = s + " lrn.statusObrade = :status";
        }

        if (request.getOdDatuma() != null) {
            if (hasAnyParams) {
                s = s + " AND";
            } else {
                s = s + " WHERE";
            }
            param.put("od", request.getOdDatuma());
            s = s + " lrn.datumVremeKreiranja >= :od";
        }

        TypedQuery<LaboratorijskiRadniNalog> query
                = entityManager.createQuery(
                s, LaboratorijskiRadniNalog.class);
        for (String t : param.keySet()) {
            query.setParameter(t, param.get(t));
        }

        query.setFirstResult(page * size - size);
        query.setMaxResults(size);

        List<LaboratorijskiRadniNalogResponseDTO> ret = new ArrayList<>();

        for (LaboratorijskiRadniNalog nalog : query.getResultList()) {
            ret.add(new LaboratorijskiRadniNalogResponseDTO(nalog));
        }

        return ok(ret);
    }

    @GetMapping(value = "/get-work-order")
    public ResponseEntity<LaboratorijskiRadniNalogResponseDTO> getLaboratorijskiRadniNalog(@RequestParam long id) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LaboratorijskiRadniNalog nalog = radniNalogService.fetchRadniNalogById(id);

        if (nalog == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ok(new LaboratorijskiRadniNalogResponseDTO(nalog));
    }

    @PutMapping(value = "/verify-work-order")
    public ResponseEntity<?> verifyLaboratorijskiRadniNalog(@RequestParam long id) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.SPEC_BIOHEMICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LaboratorijskiRadniNalog nalog = radniNalogService.fetchRadniNalogById(id);

        Map<String, Object> param = new HashMap<>();
        String s = "SELECT r from RezultatParametraAnalize r WHERE r.laboratorijskiRadniNalog = :nalog";
        param.put("nalog", nalog);

        TypedQuery<RezultatParametraAnalize> queryRezultati
                = entityManager.createQuery(
                s, RezultatParametraAnalize.class);
        for (String t : param.keySet()) {
            queryRezultati.setParameter(t, param.get(t));
        }

        boolean hasResults = true;
        String nazivAnalize = "";


        for (RezultatParametraAnalize rezultat : queryRezultati.getResultList()) {
            if (rezultat.getRezultat() == null) {
                nazivAnalize = rezultat.getParametarAnalize().getLaboratorijskaAnaliza().getNazivAnalize();
                hasResults = false;
            }
        }

        if (!hasResults) {
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

    @PostMapping(value = "/create-uput")
    public ResponseEntity<?> createUput(@RequestBody CreateUputDTO request) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);

        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ArrayList<Object> requiredParams = new ArrayList<>();
        requiredParams.add(request.getTip());
        requiredParams.add(request.getLbp());
        requiredParams.add(request.getLbz());
        requiredParams.add(request.getIzOdeljenjaId());
        requiredParams.add(request.getZaOdeljenjeId());
        for (Object param : requiredParams) {
            if (param == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(" Required param is not defined ");
            }
        }
        Uput noviUput = new Uput(request);
        uputService.saveUput(noviUput);

        return ResponseEntity.ok(new UputResponseDTO(noviUput));
    }

    @DeleteMapping(value = "/delete-uput")
    public ResponseEntity<?> deleteUput(@RequestParam long uputId) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);

        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        uputService.deleteUput(uputId);

        return ResponseEntity.ok("Uput obrisan");
    }

    @GetMapping(value = "/fetch-uput")
    public ResponseEntity<?> fetchUput(@RequestParam long uputId) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);

        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Uput uput = uputService.fetchUputById(uputId);

        return ok(uput);
    }

    @PostMapping(value = "/set-uput-status")
    public ResponseEntity<?> setUputStatus(@RequestBody SetUputStatusDTO request) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.ADMIN);
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);

        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Uput uput = uputService.fetchUputById(request.getUputId());
        uput.setStatus(request.getStatusUputa());
        uputService.saveUput(uput);

        return ok(uput);
    }

    @PostMapping(value = "/uput-history")
    public ResponseEntity<?> uputHistory(@RequestBody UputHistoryRequestDTO request,
                                         @RequestParam int page,
                                         @RequestParam int size) {

        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.DR_SPEC);
        acceptedRoles.add(Constants.DR_SPEC_POV);
        String s = "SELECT u from Uput u WHERE u.lbp = :lbp";

        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("lbp", UUID.fromString(request.getLbp()));

        if (request.getDoDatuma() != null) {
            param.put("do", request.getDoDatuma());
            s = s + " AND u.datumVremeKreiranja <= :do";
        }
        if (request.getOdDatuma() != null) {
            param.put("od", request.getOdDatuma());
            s = s + " AND u.datumVremeKreiranja >= :od";
        }
        TypedQuery<Uput> query
                = entityManager.createQuery(
                s, Uput.class);
        for (String t : param.keySet()) {
            query.setParameter(t, param.get(t));
        }
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        List<UputResponseDTO> ret = new ArrayList<>();
        for (Uput uput : query.getResultList()) {
            ret.add(new UputResponseDTO(uput));
        }


        return ok(ret);

    }

    @GetMapping(value = "/unprocessed-uputi")
    public ResponseEntity<?> unprocessedUputi(@RequestParam String lbp) {
        StatusUputa status = StatusUputa.NEREALIZOVAN;
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        String s = "SELECT u from Uput u WHERE u.lbp = :lbp AND u.status = :status";
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        param.put("lbp", UUID.fromString(lbp));


        TypedQuery<Uput> query
                = entityManager.createQuery(
                s, Uput.class);
        for (String t : param.keySet()) {
            query.setParameter(t, param.get(t));
        }
        List<UputResponseDTO> ret = new ArrayList<>();
        for (Uput uput : query.getResultList()) {
            ret.add(new UputResponseDTO(uput));
        }
        return ok(ret);
    }

    @PostMapping(value = "/unprocessed-uputi-with-type")
    public ResponseEntity<?> unprocessedUputiWithType(@RequestBody SearchUputiDTO request) {
        StatusUputa status = StatusUputa.NEREALIZOVAN;
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.VISA_MED_SESTRA);
        acceptedRoles.add(Constants.MED_SESTRA);

        if (loggedInUser.getRoles().stream().noneMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String s = "SELECT u from Uput u WHERE u.lbp = :lbp AND u.status = :status AND u.tip = :tip";
        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        param.put("tip", request.getTipUputa().toString());
        param.put("lbp", UUID.fromString(request.getLbp()));


        TypedQuery<Uput> query
                = entityManager.createQuery(
                s, Uput.class);
        for (String t : param.keySet()) {
            query.setParameter(t, param.get(t));
        }

        List<UputResponseDTO> ret = new ArrayList<>();
        for (Uput uput : query.getResultList()) {
            ret.add(new UputResponseDTO(uput));
        }
        return ok(ret);
    }

    @RequestMapping(path = "/labreportprint", method = RequestMethod.POST)
    public ResponseEntity<?> labReportPrint(@RequestBody Map<String, Object> request){
        return pdfGeneratorService.reportPrint(request);
    }
}
