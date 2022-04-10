import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.VakcinaController;
import raf.si.bolnica.management.entities.Vakcina;
import raf.si.bolnica.management.entities.Vakcinacija;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.exceptionHandler.medicalRecord.AllergenRecordExceptionHandler;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.exceptions.VaccineNotExistException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.AddVaccineToPatientRequestDTO;
import raf.si.bolnica.management.services.AlergenZdravstveniKartonService;
import raf.si.bolnica.management.services.VakcinacijaService;
import raf.si.bolnica.management.services.vakcina.VakcinaService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VaccineTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    VakcinaService vakcinaService;

    @Mock
    AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Mock
    AllergenRecordExceptionHandler allergenRecordExceptionHandler;

    @Mock
    VakcinacijaService vakcinacijaService;

    @Mock
    EntityManager entityManager;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

    @Mock
    ResponseEntity response;

    @InjectMocks
    VakcinaController vakcinaController;

    AddVaccineToPatientRequestDTO getRequest() {
        AddVaccineToPatientRequestDTO request = new AddVaccineToPatientRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setNaziv("SYNFLORIX");
        request.setDatumVakcinacije(new Date(Calendar.getInstance().getTime().getTime()));

        return request;
    }

    AddVaccineToPatientRequestDTO getRequestWithMissingField() {
        AddVaccineToPatientRequestDTO request = new AddVaccineToPatientRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setNaziv("SYNFLORIX");

        return request;
    }


    @Test
    public void whenUnauthorizedRole_ExpectStatusCode403() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddVaccineToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenMissingFieldInRequest_ExpectMissingRequestFieldsException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddVaccineToPatientRequestDTO request = getRequestWithMissingField();

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);
    }

    @Test
    public void whenVaccineNotExistInDB_ExpectAllergenNotExistException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        AddVaccineToPatientRequestDTO request = getRequest();
        request.setNaziv("Vakcina Test");

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);
        });

        assertThat(thrown).isInstanceOf(VaccineNotExistException.class)
                .hasMessageContaining("Vaccine not exist.");
    }

    @Test
    public void whenInvalidPatientLBP_ExpectStatusCode400() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(vakcinaService.findVakcinaByNaziv(any(String.class))).thenReturn(new Vakcina());
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(null);

        AddVaccineToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void whenValidRequest_ExpectStatusCode200() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(vakcinaService.findVakcinaByNaziv(any(String.class))).thenReturn(new Vakcina());
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(new ZdravstveniKarton());
        when(vakcinacijaService.save(any(Vakcinacija.class))).thenReturn(new Vakcinacija());

        AddVaccineToPatientRequestDTO request = getRequest();

        ResponseEntity<?> response = vakcinaController.addVaccineToPatient(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

}
