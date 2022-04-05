package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.bolnica.management.entities.Pregled;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreatePregledRequestDTO;
import raf.si.bolnica.management.service.PregledService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class ManagementController {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private PregledService pregledService;

    @PostMapping(value = "/create-pregled")
    public ResponseEntity<?> createPregled(@RequestBody CreatePregledRequestDTO requestDTO) {
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add("ROLE_DR_SPEC_ODELJENJA"); acceptedRoles.add("ROLE_DR_SPEC"); acceptedRoles.add("ROLE_DR_SPEC_POV");
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {

            if (loggedInUser.getRoles().contains("ROLE_DR_SPEC_POV")
                    && requestDTO.getIndikatorPoverljivosti()) {
                requestDTO.setIndikatorPoverljivosti(false);
            }

            Pregled pregledToReturn = pregledService.createPregled(requestDTO);
            return ok(pregledToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }






}
