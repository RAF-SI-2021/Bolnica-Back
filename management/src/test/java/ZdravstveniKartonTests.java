import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.LekarskiIzvestajController;
import raf.si.bolnica.management.controllers.OtpusnaListaController;
import raf.si.bolnica.management.controllers.ZdravstveniKartonController;
import raf.si.bolnica.management.entities.BolnickaSoba;
import raf.si.bolnica.management.entities.Hospitalizacija;
import raf.si.bolnica.management.entities.OtpusnaLista;
import raf.si.bolnica.management.entities.ZdravstveniKarton;
import raf.si.bolnica.management.entities.enums.KrvnaGrupa;
import raf.si.bolnica.management.entities.enums.RhFaktor;
import raf.si.bolnica.management.exceptionHandler.medicalRecord.AllergenRecordExceptionHandler;
import raf.si.bolnica.management.exceptions.MissingRequestFieldsException;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.CreateOtpusnaListaDTO;
import raf.si.bolnica.management.requests.LekarskiIzvestajDTO;
import raf.si.bolnica.management.requests.LekarskiIzvestajFilterDTO;
import raf.si.bolnica.management.requests.UpdateMedicalRecordBloodTypeRhFactorRequestDTO;
import raf.si.bolnica.management.services.AlergenZdravstveniKartonService;
import raf.si.bolnica.management.services.bolnickaSoba.BolnickaSobaService;
import raf.si.bolnica.management.services.hospitalizacija.HospitalizacijaService;
import raf.si.bolnica.management.services.lekarskiIzvestaj.LekarskiIzvestajService;
import raf.si.bolnica.management.services.otpusnaLista.OtpusnaListaService;
import raf.si.bolnica.management.services.vakcina.VakcinaService;
import raf.si.bolnica.management.services.zdravstveniKarton.ZdravstveniKartonService;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ZdravstveniKartonTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    VakcinaService vakcinaService;

    @Mock
    ZdravstveniKartonService zdravstveniKartonService;

    @Mock
    AlergenZdravstveniKartonService alergenZdravstveniKartonService;

    @Mock
    AllergenRecordExceptionHandler allergenRecordExceptionHandler;

    @Mock
    HospitalizacijaService hospitalizacijaService;

    @Mock
    BolnickaSobaService bolnickaSobaService;

    @Mock
    OtpusnaListaService otpusnaListaService;

    @InjectMocks
    OtpusnaListaController otpusnaListaController;

    @Mock
    LekarskiIzvestajService lekarskiIzvestajService;

    @InjectMocks
    LekarskiIzvestajController lekarskiIzvestajController;

    @Mock
    EntityManager entityManager;

    @Mock
    ResponseEntity response;

    @InjectMocks
    ZdravstveniKartonController zdravstveniKartonController;

    UpdateMedicalRecordBloodTypeRhFactorRequestDTO getRequest() {
        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = new UpdateMedicalRecordBloodTypeRhFactorRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setKrvnaGrupa(KrvnaGrupa.A);
        request.setRhFaktor(RhFaktor.MINUS);

        return request;
    }

    UpdateMedicalRecordBloodTypeRhFactorRequestDTO getRequestWithMissingField() {
        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = new UpdateMedicalRecordBloodTypeRhFactorRequestDTO();

        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setKrvnaGrupa(KrvnaGrupa.A);

        return request;
    }


    @Test
    public void whenUnauthorizedRole_ExpectStatusCode403() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.VISA_MED_SESTRA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = getRequest();

        ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void whenMissingFieldInRequest_ExpectMissingRequestFieldsException() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request1 = getRequest();
        request1.setLbp(null);

        Throwable thrown = catchThrowable(() -> {
            ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request1);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request2 = getRequest();
        request2.setKrvnaGrupa(null);

        thrown = catchThrowable(() -> {
            ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request2);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request3 = getRequest();
        request3.setRhFaktor(null);

        thrown = catchThrowable(() -> {
            ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request3);
        });

        assertThat(thrown).isInstanceOf(MissingRequestFieldsException.class);
    }

    @Test
    public void whenInvalidPatientLBP_ExpectStatusCode400() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(null);

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = getRequest();

        ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void whenValidRequest_ExpectStatusCode200() {
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(zdravstveniKartonService.findZdravstveniKartonByPacijentLbp(any(UUID.class))).thenReturn(new ZdravstveniKarton());
        when(zdravstveniKartonService.saveZdravstveniKarton(any(ZdravstveniKarton.class))).thenReturn(new ZdravstveniKarton());

        UpdateMedicalRecordBloodTypeRhFactorRequestDTO request = getRequest();

        ResponseEntity<?> response = zdravstveniKartonController.updatePatientMedicalRecord(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    LekarskiIzvestajDTO getRequestIzvestaj(){
        LekarskiIzvestajDTO req = new LekarskiIzvestajDTO();
        req.setLbp("237e9877-e79b-12d4-a765-321741963000");
        req.setDijagnoza("Dijagnoza");
        req.setIndikatorPoverljivosti(false);
        req.setSavet("Savet");
        req.setObjektivniNalaz("Objektivni nalaz");
        req.setPredlozenaTerapija("Predlozena terapija");

        return req;
    }

    @Test
    public void testCreateLekarskiIzvestajInvalidRequest(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        LekarskiIzvestajDTO req = getRequestIzvestaj();
        req.setLbp(null);

        ResponseEntity<?> response = lekarskiIzvestajController.registerLekarskiIzvestaj(req);

        assertThat(response.getStatusCodeValue()).isEqualTo(406);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("LBP je obavezno polje");
    }


    @Test
    public void testCreateLekarskiIzvestajSuccess(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);
        LekarskiIzvestajDTO req = getRequestIzvestaj();
        ResponseEntity<?> response = lekarskiIzvestajController.registerLekarskiIzvestaj(req);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void testSearchLekarskiIzvestaji1Success(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        LekarskiIzvestajFilterDTO lekarskiIzvestajFilterDTO = new LekarskiIzvestajFilterDTO();
        lekarskiIzvestajFilterDTO.setLbp(UUID.randomUUID().toString());
        lekarskiIzvestajFilterDTO.setDate(new Date(1000));
        lekarskiIzvestajFilterDTO.setEnd(new Date(100000));
        ResponseEntity<?> response = lekarskiIzvestajController.searchLekarskiIzvestaji(lekarskiIzvestajFilterDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void testSearchLekarskiIzvestaji2Success(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        LekarskiIzvestajFilterDTO lekarskiIzvestajFilterDTO = new LekarskiIzvestajFilterDTO();
        lekarskiIzvestajFilterDTO.setLbp(UUID.randomUUID().toString());
        lekarskiIzvestajFilterDTO.setEnd(new Date(100000));
        ResponseEntity<?> response = lekarskiIzvestajController.searchLekarskiIzvestaji(lekarskiIzvestajFilterDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void testSearchLekarskiIzvestaji3Success(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        LekarskiIzvestajFilterDTO lekarskiIzvestajFilterDTO = new LekarskiIzvestajFilterDTO();
        lekarskiIzvestajFilterDTO.setLbp(UUID.randomUUID().toString());
        lekarskiIzvestajFilterDTO.setDate(new Date(1000));
        ResponseEntity<?> response = lekarskiIzvestajController.searchLekarskiIzvestaji(lekarskiIzvestajFilterDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void testSearchLekarskiIzvestajiSuccess(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK_ODELJENJA);

        when(loggedInUser.getRoles()).thenReturn(roles);
        LekarskiIzvestajFilterDTO lekarskiIzvestajFilterDTO = new LekarskiIzvestajFilterDTO();
        lekarskiIzvestajFilterDTO.setLbp(UUID.randomUUID().toString());
        ResponseEntity<?> response = lekarskiIzvestajController.searchLekarskiIzvestaji(lekarskiIzvestajFilterDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

    }

    public CreateOtpusnaListaDTO getRequestOtpusnalista(){
        CreateOtpusnaListaDTO request = new CreateOtpusnaListaDTO();
        request.setLbp("237e9877-e79b-12d4-a765-321741963000");
        request.setAnalize("analize");
        request.setAnamneza("anamneza");
        request.setTerapija("terapija");
        request.setPbo(1);
        request.setZakljucak("zakljucak");
        request.setPrateceDijagnoze("pratece bolesti");
        request.setTokBolesti("Tok bolesti");

        return request;
    }

    @Test
    public void testCreateOtpusnaListaInvalidRequest(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.SPECIJLISTA_POV);

        when(loggedInUser.getRoles()).thenReturn(roles);

        CreateOtpusnaListaDTO request = getRequestOtpusnalista();
        request.setLbp(null);

        ResponseEntity<?> response = otpusnaListaController.registerOtpusnaLista(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(406);

        assertThat(response.getBody()).isInstanceOf(String.class);

        assertThat(response.getBody()).isEqualTo("Lbp je obavezno polje");
    }

    @Test
    public void testCreateOtpusnaListaRequest(){
        Set<String> roles = new TreeSet<>();

        roles.add(Constants.NACELNIK);

        when(loggedInUser.getRoles()).thenReturn(roles);
        when(hospitalizacijaService.findCurrentByLbp(any(UUID.class))).thenAnswer(i-> new Hospitalizacija());
        when(hospitalizacijaService.save(any(Hospitalizacija.class))).thenAnswer(i -> new Hospitalizacija());
        when(bolnickaSobaService.findById(any(Long.class))).thenAnswer(i -> new BolnickaSoba());
        when(bolnickaSobaService.decrement(any(BolnickaSoba.class))).thenAnswer(i -> 1);
        when(bolnickaSobaService.save(any(BolnickaSoba.class))).thenAnswer(i -> new BolnickaSoba());
        when(otpusnaListaService.save(any(OtpusnaLista.class))).thenAnswer(i -> new OtpusnaLista());

        CreateOtpusnaListaDTO request = getRequestOtpusnalista();

        ResponseEntity<?> response = otpusnaListaController.registerOtpusnaLista(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

}
