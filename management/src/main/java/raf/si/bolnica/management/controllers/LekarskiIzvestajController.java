package raf.si.bolnica.management.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.LekarskiIzvestajStacionar;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.LekarskiIzvestajDTO;
import raf.si.bolnica.management.requests.LekarskiIzvestajDTORequestValidator;
import raf.si.bolnica.management.requests.LekarskiIzvestajFilterDTO;
import raf.si.bolnica.management.requests.LekarskiIzvestajFilterDTORequestValidator;
import raf.si.bolnica.management.services.lekarskiIzvestaj.LekarskiIzvestajService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class LekarskiIzvestajController {

    @Autowired
    LekarskiIzvestajService lekarskiIzvestajService;

    @Autowired
    LoggedInUser loggedInUser;

    @PostMapping(value = Constants.REGISTER_LEKARSKI_IZVESTAJ)
    public ResponseEntity<?> registerLekarskiIzvestaj(@RequestBody LekarskiIzvestajDTO lekarskiIzvestajDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.SPECIJALISTA);
        acceptedRoles.add(Constants.SPECIJLISTA_POV);

        if (loggedInUser.getRoles().stream().noneMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String msg = LekarskiIzvestajDTORequestValidator.validate(lekarskiIzvestajDTO);
        if (!msg.equals("OK")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(msg);
        }
        if (lekarskiIzvestajDTO.isIndikatorPoverljivosti() && !loggedInUser.getRoles().contains(Constants.SPECIJLISTA_POV)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nemate privilegije za postavljanje poverljivosti");
        }

        LekarskiIzvestajStacionar lekarskiIzvestajStacionar = new LekarskiIzvestajStacionar();

        lekarskiIzvestajStacionar.setLbzLekaraSpecijaliste(loggedInUser.getLBZ());
        lekarskiIzvestajStacionar.setLbpPacijenta(UUID.fromString(lekarskiIzvestajDTO.getLbp()));
        lekarskiIzvestajStacionar.setDijagnoza(lekarskiIzvestajDTO.getDijagnoza());
        lekarskiIzvestajStacionar.setIndikatorPoverljivosti(lekarskiIzvestajDTO.isIndikatorPoverljivosti());
        lekarskiIzvestajStacionar.setDatumVremeKreiranja(new Date(Calendar.getInstance().getTime().getTime()));
        lekarskiIzvestajStacionar.setSavet(lekarskiIzvestajDTO.getSavet());
        lekarskiIzvestajStacionar.setObjektivniNalaz(lekarskiIzvestajDTO.getObjektivniNalaz());
        lekarskiIzvestajStacionar.setPredlozenaTerapija(lekarskiIzvestajDTO.getPredlozenaTerapija());

        lekarskiIzvestajService.save(lekarskiIzvestajStacionar);

        return ResponseEntity.ok().body(lekarskiIzvestajStacionar);
    }

    @PostMapping(value = Constants.SEARCH_LEKARSKI_IZVESTAJ)
    public ResponseEntity<?> searchLekarskiIzvestaji(@RequestBody LekarskiIzvestajFilterDTO lekarskiIzvestajFilterDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.SPECIJALISTA);
        acceptedRoles.add(Constants.SPECIJLISTA_POV);
        if (loggedInUser.getRoles().stream().noneMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String msg = LekarskiIzvestajFilterDTORequestValidator.validate(lekarskiIzvestajFilterDTO);
        if (!msg.equals("OK")) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(msg);
        }
        boolean indikator = loggedInUser.getRoles().contains(Constants.SPECIJLISTA_POV);
        int type = LekarskiIzvestajFilterDTORequestValidator.checkRequest(lekarskiIzvestajFilterDTO);
        List<LekarskiIzvestajStacionar> lekarskiIzvestajStacionars = new ArrayList<>();
        switch (type) {
            case 0:
                lekarskiIzvestajStacionars = lekarskiIzvestajService.findByLBP(UUID.fromString(lekarskiIzvestajFilterDTO.getLbp()), indikator);
                break;
            case 1:
                lekarskiIzvestajStacionars = lekarskiIzvestajService.findByLBPAndDate(UUID.fromString(lekarskiIzvestajFilterDTO.getLbp()), lekarskiIzvestajFilterDTO.getDate(), indikator);
                break;
            case 2:
                lekarskiIzvestajStacionars = lekarskiIzvestajService.findByLBPAndBetweenDates(UUID.fromString(lekarskiIzvestajFilterDTO.getLbp()), lekarskiIzvestajFilterDTO.getDate(), lekarskiIzvestajFilterDTO.getEnd(), indikator);
        }
        return ResponseEntity.ok().body(lekarskiIzvestajStacionars);
    }
}
