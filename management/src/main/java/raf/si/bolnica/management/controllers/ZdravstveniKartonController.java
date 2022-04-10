package raf.si.bolnica.management.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.UpdateMedicalRecordBloodTypeRhFactorRequestDTO;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class ZdravstveniKartonController {

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private ZdravstveniKartonService zdravstveniKartonService;

    @PutMapping(value = Constants.UPDATE_PATIENT_MEDICAL_RECORD_BLOODTYPE_RHFACTOR)
    public ResponseEntity<?> updatePatientMedicalRecord(@RequestBody UpdateMedicalRecordBloodTypeRhFactorRequestDTO requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_DR_SPEC_ODELJENJA") ||
                loggedInUser.getRoles().contains("ROLE_NACELNIK_ODELJENJA")) {

            if (requestDTO.getLbp() == null || requestDTO.getKrvnaGrupa() == null || requestDTO.getRhFaktor() == null)
                throw new MissingRequestFieldsException(Constants.MissingRequestFields.MESSAGE, Constants.MissingRequestFields.DEVELOPER_MESSAGE);

            ZdravstveniKarton zdravstveniKarton = zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(UUID.fromString(requestDTO.getLbp()));
            if (zdravstveniKarton == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            zdravstveniKarton.setKrvnaGrupa(requestDTO.getKrvnaGrupa());
            zdravstveniKarton.setRhFaktor(requestDTO.getRhFaktor());

            ZdravstveniKarton zdravstveniKartonToReturn = zdravstveniKartonService.saveZdravstveniKarton(zdravstveniKarton);

            return ResponseEntity.ok(zdravstveniKartonToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
