package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.Alergen;
import raf.si.bolnica.management.entities.AlergenZdravstveniKarton;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.exceptions.AllergenNotExistException;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.AddAllergentToPatientRequestDTO;
import raf.si.bolnica.management.response.AlegrenZdravstveniKartonDto;
import raf.si.bolnica.management.services.AlergenZdravstveniKartonService;
import raf.si.bolnica.management.services.alergen.AlergenService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        List<String> acceptedRoles = new ArrayList<>();
        acceptedRoles.add(Constants.NACELNIK_ODELJENJA);
        acceptedRoles.add(Constants.SPECIJALISTA);
        if (loggedInUser.getRoles().stream().anyMatch(acceptedRoles::contains)) {

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

                if (zdravstveniKarton == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                for (AlergenZdravstveniKarton alergenZdravstveniKarton : zdravstveniKarton.getAlergenZdravstveniKarton()) {
                    if (alergenZdravstveniKarton.getAlergen().getNaziv().equals(requestDTO.getNaziv())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }
                AlergenZdravstveniKarton alergenZdravstveniKarton = new AlergenZdravstveniKarton();
                alergenZdravstveniKarton.setAlergen(allergen);
                alergenZdravstveniKarton.setZdravstveniKarton(zdravstveniKarton);
                alergenZdravstveniKarton.setObrisan(false);

                AlergenZdravstveniKarton alergenZdravstveniKartonToReturn =
                        alergenZdravstveniKartonService.save(alergenZdravstveniKarton);

                zdravstveniKarton.getAlergenZdravstveniKarton().add(alergenZdravstveniKarton);
                zdravstveniKartonService.saveZdravstveniKarton(zdravstveniKarton);

                return ResponseEntity.ok(new AlegrenZdravstveniKartonDto(alergenZdravstveniKartonToReturn, zdravstveniKarton.getZdravstveniKartonId()));
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
