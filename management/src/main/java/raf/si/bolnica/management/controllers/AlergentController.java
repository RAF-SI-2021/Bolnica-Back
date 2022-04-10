package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.Alergen;
import raf.si.bolnica.management.entities.AlergenZdravstveniKarton;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.exceptions.AllergenNotExistException;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.AddAllergentToPatientRequestDTO;
import raf.si.bolnica.management.services.AlergenZdravstveniKartonService;
import raf.si.bolnica.management.services.alergen.AlergenService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class AlergentController {

    @Autowired
    private AlergenService alergenService;

    @Autowired
    private AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private ZdravstveniKartonService zdravstveniKartonService;

    @PostMapping(value = Constants.ADD_ALLERGEN_TO_PATIENT)
    public ResponseEntity<?> addAllergenToPatient(@RequestBody AddAllergentToPatientRequestDTO requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_DR_SPEC_ODELJENJA") ||
                loggedInUser.getRoles().contains("ROLE_NACELNIK_ODELJENJA")) {

            if (requestDTO.getNaziv() == null || requestDTO.getLbp() == null)
                throw new MissingRequestFieldsException(Constants.MissingRequestFields.MESSAGE, Constants.MissingRequestFields.DEVELOPER_MESSAGE);

            Alergen allergen = alergenService.findAlergenByNaziv(requestDTO.getNaziv());

            if (allergen == null) {
                throw new AllergenNotExistException(
                        Constants.AllergenNotExist.MESSAGE,
                        Constants.AllergenNotExist.DEVELOPER_MESSAGE
                );
            } else {
                ZdravstveniKarton zdravstveniKarton = zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(UUID.fromString(requestDTO.getLbp()));
                System.out.println(zdravstveniKarton);

                if (zdravstveniKarton == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                AlergenZdravstveniKarton alergenZdravstveniKarton = new AlergenZdravstveniKarton();
                alergenZdravstveniKarton.setAlergen(allergen);
                alergenZdravstveniKarton.setZdravstveniKarton(zdravstveniKarton);

                AlergenZdravstveniKarton alergenZdravstveniKartonToReturn =
                        alergenZdravstveniKartonService.save(alergenZdravstveniKarton);

                return ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
