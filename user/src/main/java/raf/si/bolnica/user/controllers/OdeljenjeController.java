package raf.si.bolnica.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.service.OdeljenjeService;

import java.util.*;


@RestController
@RequestMapping(value = Constants.BASE_API)
public class OdeljenjeController {

    @Autowired
    private OdeljenjeService odeljenjeService;

    @Autowired
    private LoggedInUser loggedInUser;

    @GetMapping(value = Constants.SEARCH_DEPARTMENT_BY_NAME)
    public ResponseEntity<List<Odeljenje>> searchForDepartmentByName(@RequestParam String name) {
        if (loggedInUser.getRoles().contains("ROLE_NACELNIK_ODELJENJA") ||
                loggedInUser.getRoles().contains("ROLE_DR_SPEC_ODELJENJA") ||
                loggedInUser.getRoles().contains("ROLE_DR_SPEC_POV") ||
                loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            List<Odeljenje> departmentsToReturn;
            if (name.isEmpty()) {
                departmentsToReturn = odeljenjeService.findAll();
            } else {
                departmentsToReturn = odeljenjeService.searchByNaziv(name);
            }
            if (departmentsToReturn.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(departmentsToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = Constants.ALL_DEPARTMENTS)
    public ResponseEntity<List<Odeljenje>> getAllDepartments() {
        if (loggedInUser.getRoles().contains("ROLE_DR_SPEC") ||
                loggedInUser.getRoles().contains("ROLE_DR_SPEC_POV") ||
                loggedInUser.getRoles().contains("ROLE_VISA_MED_SESTRA") ||
                loggedInUser.getRoles().contains("ROLE_MED_SESTRA") ||
                loggedInUser.getRoles().contains("ROLE_RECEPCIONER") ||
                loggedInUser.getRoles().contains("ROLE_ADMIN")) {

            List<Odeljenje> departments = odeljenjeService.findAllByPbb();

            return ResponseEntity.ok(departments);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = Constants.ALL_HOSPITALS)
    public ResponseEntity<List<ZdravstvenaUstanova>> getAllHospitals() {
        if (loggedInUser.getRoles().contains("ROLE_DR_SPEC") ||
                loggedInUser.getRoles().contains("ROLE_DR_SPEC_POV") ||
                loggedInUser.getRoles().contains("ROLE_VISA_MED_SESTRA") ||
                loggedInUser.getRoles().contains("ROLE_MED_SESTRA") ||
                loggedInUser.getRoles().contains("ROLE_RECEPCIONER") ||
                loggedInUser.getRoles().contains("ROLE_ADMIN")) {

            List<ZdravstvenaUstanova> hospitals = odeljenjeService.findAllHospitals();

            return ResponseEntity.ok(hospitals);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
