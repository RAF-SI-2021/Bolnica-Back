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
import raf.si.bolnica.management.services.bolnickaSoba.BolnickaSobaService;
import raf.si.bolnica.management.services.hospitalizacija.HospitalizacijaService;
import raf.si.bolnica.management.services.otpusnaLista.OtpusnaListaService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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


    @PostMapping(value = Constants.Register_OTPUSNA_LISTA)
    ResponseEntity<?> registerOtpusnaLista(@RequestBody CreateOtpusnaListaDTO req){

        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.SPECIJALISTA);
        acceptedRoles.add(Constants.SPECIJLISTA_POV);

        if(!loggedInUser.getRoles().stream().anyMatch(acceptedRoles :: contains)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String msg = CreateOtpusnaListaDTOValidator.validate(req);
        if(!msg.equals("OK")){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
        }

        UUID lbzNacelnika = loggedInUser.getLBZ();

        if(!loggedInUser.getRoles().stream().anyMatch(Constants.NACELNIK :: contains)){
            String uri = "http://host.docker.internal:8081/api/find-dr-spec-odeljenja/" + req.getPbo();
            RestTemplate restTemplate = new RestTemplate();
            lbzNacelnika = UUID.fromString(restTemplate.getForObject(uri, String.class));
        } else{
            lbzNacelnika = loggedInUser.getLBZ();
        }

        Hospitalizacija hospitalizacija = hospitalizacijaService.findCurrentByLbp(UUID.fromString(req.getLbp()));
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        otpusnaLista.setDatumVremeKreiranja(timestamp);
        hospitalizacija.setDatumVremeOtpustanja(timestamp);

        BolnickaSoba bolnickaSoba = bolnickaSobaService.findById(hospitalizacija.getBolnickaSobaId());
        bolnickaSobaService.decrement(bolnickaSoba);
        bolnickaSobaService.save(bolnickaSoba);

        hospitalizacijaService.save(hospitalizacija);
        otpusnaListaService.save(otpusnaLista);




        return ResponseEntity.ok().body(otpusnaLista);
    }

}
