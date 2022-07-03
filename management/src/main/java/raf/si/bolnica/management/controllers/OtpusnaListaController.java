package raf.si.bolnica.management.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.BolnickaSoba;
import raf.si.bolnica.management.entities.Hospitalizacija;
import raf.si.bolnica.management.entities.OtpusnaLista;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateOtpusnaListaDTO;
import raf.si.bolnica.management.requests.CreateOtpusnaListaDTOValidator;
import raf.si.bolnica.management.requests.OtpusnaLIstaFilterDTO;
import raf.si.bolnica.management.requests.OtpusnaListaFilterDTORequestValidator;
import raf.si.bolnica.management.response.OtpusnaListaResponseDTO;
import raf.si.bolnica.management.services.bolnickaSoba.BolnickaSobaService;
import raf.si.bolnica.management.services.hospitalizacija.HospitalizacijaService;
import raf.si.bolnica.management.services.otpusnaLista.OtpusnaListaService;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class OtpusnaListaController {

    @Autowired
    LoggedInUser loggedInUser;

    @Autowired
    OtpusnaListaService otpusnaListaService;

    @Autowired
    HospitalizacijaService hospitalizacijaService;

    @Autowired
    BolnickaSobaService bolnickaSobaService;


    @PostMapping(value = Constants.REGISTER_OTPUSNA_LISTA)
    public ResponseEntity<?> registerOtpusnaLista(@RequestBody CreateOtpusnaListaDTO req) {

        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK);
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.SPECIJALISTA);
        acceptedRoles.add(Constants.SPECIJLISTA_POV);

        if (loggedInUser.getRoles().stream().noneMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String msg = CreateOtpusnaListaDTOValidator.validate(req);
        if (!msg.equals("OK")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(msg);
        }

        UUID lbzNacelnika;

        if (loggedInUser.getRoles().stream().noneMatch(Constants.NACELNIK::contains)) {
            String uri = "http://bolnica.k8s.elab.rs:32264/bolnica-user/api/find-dr-spec-odeljenja/" + req.getPbo();
            RestTemplate restTemplate = new RestTemplate();
            lbzNacelnika = UUID.fromString(Objects.requireNonNull(restTemplate.getForObject(uri, String.class)));
        } else {
            lbzNacelnika = loggedInUser.getLBZ();
        }

        Hospitalizacija hospitalizacija = hospitalizacijaService.findCurrentByLbp(UUID.fromString(req.getLbp()));
        if (hospitalizacija == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Pacijent nije trenutno hospitalizovan");
        }
        OtpusnaLista otpusnaLista = new OtpusnaLista();
        otpusnaLista.setLbpPacijenta(UUID.fromString(req.getLbp()));
        otpusnaLista.setLbzOrdinirajucegLekara(loggedInUser.getLBZ());
        otpusnaLista.setHospitalizacijaId(hospitalizacija.getHospitalizacijaId());
        otpusnaLista.setLbzNacelnikOdeljenja(lbzNacelnika);
        otpusnaLista.setPrateceDijagnoze(req.getPrateceDijagnoze());
        otpusnaLista.setAnamneza(req.getAnamneza());
        otpusnaLista.setAnalize(req.getAnalize());
        otpusnaLista.setTokBolesti(req.getTokBolesti());
        otpusnaLista.setZakljucak(req.getZakljucak());
        otpusnaLista.setTerapija(req.getTerapija());
        Date date = new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        otpusnaLista.setDatumVremeKreiranja(date);
        hospitalizacija.setDatumVremeOtpustanja(timestamp);

        BolnickaSoba bolnickaSoba = bolnickaSobaService.findById(hospitalizacija.getBolnickaSobaId());
        bolnickaSobaService.decrement(bolnickaSoba);
        bolnickaSobaService.save(bolnickaSoba);

        hospitalizacijaService.save(hospitalizacija);
        otpusnaListaService.save(otpusnaLista);


        return ResponseEntity.ok().body(otpusnaLista);
    }

    @PostMapping(value = Constants.SEARCH_OTPUSNA_LISTA)
    public ResponseEntity<?> searchOtpusneListe(@RequestBody OtpusnaLIstaFilterDTO req) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.SPECIJALISTA);
        acceptedRoles.add(Constants.SPECIJLISTA_POV);

        if (loggedInUser.getRoles().stream().noneMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        String msg = OtpusnaListaFilterDTORequestValidator.validate(req);
        if (!msg.equals("OK")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(msg);
        }
        UUID lbp = UUID.fromString(req.getLbp());
        Integer type = OtpusnaListaFilterDTORequestValidator.checkRequest(req);
        List<OtpusnaLista> otpusnaListaList = new ArrayList<>();
        switch (type) {
            case 0:
                otpusnaListaList = otpusnaListaService.findByLBP(lbp);
                break;
            case 1:
                otpusnaListaList = otpusnaListaService.findByLBPAndDate(lbp, req.getStart());
                break;
            case 2:
                otpusnaListaList = otpusnaListaService.findByLBPAndBetweenDates(lbp, req.getStart(), req.getEnd());
                break;

        }
        List<Hospitalizacija> hospitalizacijaList = hospitalizacijaService.findByLbp(UUID.fromString(req.getLbp()));
        List<OtpusnaListaResponseDTO> responseDTOS = new ArrayList<>();
        for (int i = 0; i < otpusnaListaList.size(); i++) {
            OtpusnaListaResponseDTO o = new OtpusnaListaResponseDTO();
            String uri1 = "http://bolnica.k8s.elab.rs:32264/bolnica-user/api/employee-info/" + otpusnaListaList.get(i).getLbzNacelnikOdeljenja();
            String uri2 = "http://bolnica.k8s.elab.rs:32264/bolnica-user/api/employee-info/" + otpusnaListaList.get(i).getLbzOrdinirajucegLekara();
            RestTemplate restTemplate = new RestTemplate();
            Object nacelnik = restTemplate.getForObject(uri1, Object.class);
            Object ordinirajuciLekar = restTemplate.getForObject(uri2, Object.class);
            o.setNacelnikOdeljenja(nacelnik);
            o.setOrdinirajuciLekar(ordinirajuciLekar);
            o.setOtpusnaLista(otpusnaListaList.get(i));
            o.setDatumPrijema(hospitalizacijaList.get(i).getDatumVremePrijema());
            o.setDatumOtpustanja(hospitalizacijaList.get(i).getDatumVremeOtpustanja());
            responseDTOS.add(o);


        }
        return ResponseEntity.ok().body(responseDTOS);
    }


}
