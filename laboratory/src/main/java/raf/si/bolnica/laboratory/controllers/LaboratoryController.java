package raf.si.bolnica.laboratory.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.laboratory.constants.Constants;
import raf.si.bolnica.laboratory.entities.ZakazanLaboratorijskiPregled;
import raf.si.bolnica.laboratory.entities.enums.StatusPregleda;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.requests.FindScheduledLabExaminationsDTO;
import raf.si.bolnica.laboratory.requests.ScheduleLabExaminationDTO;
import raf.si.bolnica.laboratory.requests.SetStatusExaminationDTO;
import raf.si.bolnica.laboratory.services.*;

import javax.websocket.server.PathParam;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping(value = "/schedule-lab-examination")
    public ResponseEntity<String> scheduleLabExamination(@RequestBody ScheduleLabExaminationDTO request){
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        ZakazanLaboratorijskiPregled pregled = new ZakazanLaboratorijskiPregled();
        pregled.setLbp(request.getLbp());
        pregled.setNapomena(request.getNapomena());
        pregled.setZakazanDatum(request.getDate());
        pregled.setLbz(loggedInUser.getLBZ());
        pregled.setOdeljenjeId(loggedInUser.getOdeljenjeId());
        zakazanLaboratorijskiPregledService.saveZakazanPregled(pregled);

        return ResponseEntity.ok("Laboratorijska analiza zakazana za: " + request.getLbp() + "\nDatuma: " + request.getDate().toString() + "\nNapomena: " + request.getNapomena() + "\nNa odeljenju: " + pregled.getOdeljenjeId());
    }

    @GetMapping(value = "/get-lab-examinations/{date}")
    public ResponseEntity<Integer> getLabExaminationsOnDate(@PathVariable Date date){
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(zakazanLaboratorijskiPregledService.getZakazaniPreglediByDate(date).size());
    }

    @PostMapping(value = "/get-lab-examinations")
    public ResponseEntity<List<ZakazanLaboratorijskiPregled>> getLabExaminations(@RequestBody FindScheduledLabExaminationsDTO findScheduledLabExaminationsDTO){
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(findScheduledLabExaminationsDTO.getDate() != null && findScheduledLabExaminationsDTO.getLbp() != null){
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndZakazanDatumAndLbp(loggedInUser.getOdeljenjeId(), findScheduledLabExaminationsDTO.getDate(), findScheduledLabExaminationsDTO.getLbp()));
        } else if(findScheduledLabExaminationsDTO.getDate() != null){
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndZakazanDatum(loggedInUser.getOdeljenjeId(), findScheduledLabExaminationsDTO.getDate()));
        } else if(findScheduledLabExaminationsDTO.getLbp() != null){
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndLbp(loggedInUser.getOdeljenjeId(), findScheduledLabExaminationsDTO.getLbp()));
        } else{
            return ResponseEntity.ok(zakazanLaboratorijskiPregledService.findByOdeljenjeId(loggedInUser.getOdeljenjeId()));
        }
    }

    @PutMapping(value = "/set-lab-examination-status")
    public ResponseEntity<?> setLabExaminationStatus(@RequestBody SetStatusExaminationDTO request){
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.LABORATORIJSKI_TEHNICAR);
        acceptedRoles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        if (!loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        ZakazanLaboratorijskiPregled zakazanLaboratorijskiPregled = zakazanLaboratorijskiPregledService.getZakazanPregled(request.getId());

        zakazanLaboratorijskiPregled.setStatusPregleda(StatusPregleda.valueOf(request.getStatus()));
        zakazanLaboratorijskiPregledService.saveZakazanPregled(zakazanLaboratorijskiPregled);
        return ResponseEntity.ok("Status successfully changed to: " + zakazanLaboratorijskiPregled.getStatusPregleda().toString());

    }






}
