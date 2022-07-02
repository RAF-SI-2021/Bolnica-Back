import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.management.constants.Constants;
import raf.si.bolnica.management.controllers.ManagementController;
import raf.si.bolnica.management.entities.*;
import raf.si.bolnica.management.entities.enums.StatusTermina;
import raf.si.bolnica.management.interceptors.LoggedInUser;
import raf.si.bolnica.management.requests.*;
import raf.si.bolnica.management.services.PosetaPacijentuService;
import raf.si.bolnica.management.services.StanjePacijentaService;
import raf.si.bolnica.management.services.ZakazaniTerminPrijemaService;
import raf.si.bolnica.management.services.bolnickaSoba.BolnickaSobaService;
import raf.si.bolnica.management.services.hospitalizacija.HospitalizacijaService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ZakazaniTerminiPrijemaTests {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    EntityManager entityManager;

    @Mock
    ZakazaniTerminPrijemaService zakazaniTerminPrijemaService;

    @Mock
    StanjePacijentaService stanjePacijentaService;

    @Mock
    HospitalizacijaService hospitalizacijaService;

    @Mock
    BolnickaSobaService bolnickaSobaService;

    @Mock
    PosetaPacijentuService posetaPacijentuService;

    @InjectMocks
    ManagementController managementController;

    @Test
    public void testCreatePrijemUnauthorizedTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.SPECIJALISTA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        CreateZakazaniTerminPrijemaRequestDTO requestDTO = new CreateZakazaniTerminPrijemaRequestDTO();
        requestDTO.setNapomena("Napomena");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setDatumVremePrijema(Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<?> response = managementController.createTerminPrijema(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testCreatePrijemSuccessTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        ZakazaniTerminPrijema terminPrijema = new ZakazaniTerminPrijema();
        terminPrijema.setLbzZaposlenog(loggedInUser.getLBZ());
        terminPrijema.setLbpPacijenta(UUID.randomUUID());
        terminPrijema.setOdeljenjeId(loggedInUser.getOdeljenjeId());
        terminPrijema.setNapomena("");
        terminPrijema.setDatumVremePrijema(new Timestamp(1000));
        terminPrijema.setStatusTermina(StatusTermina.ZAKAZAN);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(zakazaniTerminPrijemaService.save(any(ZakazaniTerminPrijema.class))).thenAnswer(i -> terminPrijema);
        CreateZakazaniTerminPrijemaRequestDTO requestDTO = new CreateZakazaniTerminPrijemaRequestDTO();
        requestDTO.setNapomena("Napomena");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setDatumVremePrijema(Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<?> response = managementController.createTerminPrijema(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testCreatePrijemForbiddenTest() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        CreateZakazaniTerminPrijemaRequestDTO requestDTO = new CreateZakazaniTerminPrijemaRequestDTO();
        requestDTO.setNapomena("Napomena");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setDatumVremePrijema(Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<?> response = managementController.createTerminPrijema(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testGetTerminiPrijemaTest() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        GetPrijemiDto requestDTO = new GetPrijemiDto();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setDate("2022-07-03");
        ResponseEntity<?> response = managementController.getTerminiPrijema(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testGetTerminiPrijemaForbiddenTest() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        GetPrijemiDto requestDTO = new GetPrijemiDto();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setDate("2022-07-03");
        ResponseEntity<?> response = managementController.getTerminiPrijema(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testUpdateStatus() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateZakazaniTerminPrijemaStatusDTO updateZakazaniTerminPrijemaStatusDTO = new UpdateZakazaniTerminPrijemaStatusDTO();
        updateZakazaniTerminPrijemaStatusDTO.setStatus(StatusTermina.ZAKAZAN);
        updateZakazaniTerminPrijemaStatusDTO.setId(1);
        ResponseEntity<?> response = managementController.updateStatus(updateZakazaniTerminPrijemaStatusDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testUpdateStatusForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        UpdateZakazaniTerminPrijemaStatusDTO updateZakazaniTerminPrijemaStatusDTO = new UpdateZakazaniTerminPrijemaStatusDTO();
        updateZakazaniTerminPrijemaStatusDTO.setStatus(StatusTermina.ZAKAZAN);
        updateZakazaniTerminPrijemaStatusDTO.setId(1);
        ResponseEntity<?> response = managementController.updateStatus(updateZakazaniTerminPrijemaStatusDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testSearchPatientStateHistory1() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            return mock(TypedQuery.class);
        });
        SearchPatientStateHistoryDTO searchPatientStateHistoryDTO = new SearchPatientStateHistoryDTO();
        searchPatientStateHistoryDTO.setLbp(UUID.randomUUID());
        searchPatientStateHistoryDTO.setDoDatuma(new Timestamp(1000));
        searchPatientStateHistoryDTO.setOdDatuma(new Timestamp(10));
        ResponseEntity<?> response = managementController.searchPatientStateHistory(searchPatientStateHistoryDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSearchPatientStateHistory2() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            return mock(TypedQuery.class);
        });
        SearchPatientStateHistoryDTO searchPatientStateHistoryDTO = new SearchPatientStateHistoryDTO();
        searchPatientStateHistoryDTO.setLbp(UUID.randomUUID());
        searchPatientStateHistoryDTO.setOdDatuma(new Timestamp(10));
        ResponseEntity<?> response = managementController.searchPatientStateHistory(searchPatientStateHistoryDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSearchPatientStateHistory3() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            return mock(TypedQuery.class);
        });
        SearchPatientStateHistoryDTO searchPatientStateHistoryDTO = new SearchPatientStateHistoryDTO();
        searchPatientStateHistoryDTO.setLbp(UUID.randomUUID());
        searchPatientStateHistoryDTO.setDoDatuma(new Timestamp(1000));
        ResponseEntity<?> response = managementController.searchPatientStateHistory(searchPatientStateHistoryDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSearchPatientStateHistory4() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            return mock(TypedQuery.class);
        });
        SearchPatientStateHistoryDTO searchPatientStateHistoryDTO = new SearchPatientStateHistoryDTO();
        searchPatientStateHistoryDTO.setLbp(UUID.randomUUID());
        ResponseEntity<?> response = managementController.searchPatientStateHistory(searchPatientStateHistoryDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSearchPatientStateHistoryForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchPatientStateHistoryDTO searchPatientStateHistoryDTO = new SearchPatientStateHistoryDTO();
        searchPatientStateHistoryDTO.setLbp(UUID.randomUUID());
        searchPatientStateHistoryDTO.setDoDatuma(new Timestamp(1000));
        searchPatientStateHistoryDTO.setOdDatuma(new Timestamp(10));
        ResponseEntity<?> response = managementController.searchPatientStateHistory(searchPatientStateHistoryDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testSetPatientsState() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(stanjePacijentaService.saveStanje(any(StanjePacijenta.class))).thenAnswer(i -> new StanjePacijenta());
        SetPatientsStateDTO requestDTO = new SetPatientsStateDTO();
        requestDTO.setDatumVreme(new Timestamp(100));
        requestDTO.setLbpPacijenta(UUID.randomUUID());
        requestDTO.setKrvniPritisak("a");
        requestDTO.setOpis("a");
        requestDTO.setPuls("a");
        requestDTO.setPrimenjeneTerapije("a");
        requestDTO.setLbzRegistratora(UUID.randomUUID());
        ResponseEntity<?> response = managementController.setPatientsState(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSetPatientsStateForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        SetPatientsStateDTO requestDTO = new SetPatientsStateDTO();
        requestDTO.setDatumVreme(new Timestamp(100));
        requestDTO.setLbpPacijenta(UUID.randomUUID());
        requestDTO.setKrvniPritisak("a");
        requestDTO.setOpis("a");
        requestDTO.setPuls("a");
        requestDTO.setPrimenjeneTerapije("a");
        requestDTO.setLbzRegistratora(UUID.randomUUID());
        ResponseEntity<?> response = managementController.setPatientsState(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testSearchHospitalizedPatients1() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            return mock(TypedQuery.class);
        });
        SearchHospitalizedPatientsDTO requestDTO = new SearchHospitalizedPatientsDTO();
        requestDTO.setJmbg("A");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setName("a");
        requestDTO.setPbo(1);
        requestDTO.setSurname("b");
        ResponseEntity<?> response = managementController.searchHospitalizedPatients(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSearchHospitalizedPatientsForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchHospitalizedPatientsDTO requestDTO = new SearchHospitalizedPatientsDTO();
        requestDTO.setJmbg("A");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setName("a");
        requestDTO.setPbo(1);
        requestDTO.setSurname("b");
        ResponseEntity<?> response = managementController.searchHospitalizedPatients(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testSearchPatientsInHospital() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            return mock(TypedQuery.class);
        });
        SearchPatientsInHospitalDTO requestDTO = new SearchPatientsInHospitalDTO();
        requestDTO.setJmbg("A");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setName("a");
        requestDTO.setPbb(1);
        requestDTO.setSurname("b");
        ResponseEntity<?> response = managementController.searchPatientsInHospital(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSearchPatientsInHospitalForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        SearchPatientsInHospitalDTO requestDTO = new SearchPatientsInHospitalDTO();
        requestDTO.setJmbg("A");
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setName("a");
        requestDTO.setPbb(1);
        requestDTO.setSurname("b");
        ResponseEntity<?> response = managementController.searchPatientsInHospital(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testHospitalizePatient() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(hospitalizacijaService.save(any(Hospitalizacija.class))).thenAnswer(i -> new Hospitalizacija());
        when(bolnickaSobaService.save(any(BolnickaSoba.class))).thenAnswer(i -> new BolnickaSoba());
        when(bolnickaSobaService.findById(any(Long.class))).thenAnswer(i -> new BolnickaSoba());
        when(bolnickaSobaService.increment(any(BolnickaSoba.class))).thenAnswer(i -> 1);
        HospitalizePatientDTO requestDTO = new HospitalizePatientDTO();
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setNapomena("a");
        requestDTO.setBolnickaSobaId(1);
        requestDTO.setLbzLekara(UUID.randomUUID());
        requestDTO.setUputId(1);
        requestDTO.setUputnaDijagnoza("a");
        ResponseEntity<?> response = managementController.hospitalizePatient(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testHospitalizePatientForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        HospitalizePatientDTO requestDTO = new HospitalizePatientDTO();
        requestDTO.setLbp(UUID.randomUUID());
        requestDTO.setNapomena("a");
        requestDTO.setBolnickaSobaId(1);
        requestDTO.setLbzLekara(UUID.randomUUID());
        requestDTO.setUputId(1);
        requestDTO.setUputnaDijagnoza("a");
        ResponseEntity<?> response = managementController.hospitalizePatient(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testGetHospitalRoomsForDepartmentId() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(bolnickaSobaService.findAllByDepartmentId(any(Long.class))).thenAnswer(i -> new ArrayList<>());
        ResponseEntity<?> response = managementController.getHospitalRoomsForDepartmentId(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testGetHospitalRoomsForDepartmentIdForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.getHospitalRoomsForDepartmentId(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testGetAllPatientVisitsByLBP() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(posetaPacijentuService.findAllByLBP(any(UUID.class))).thenAnswer(i -> new ArrayList<>());
        ResponseEntity<?> response = managementController.getAllPatientVisitsByLBP(UUID.randomUUID().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testGetAllPatientVisitsByLBPForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        ResponseEntity<?> response = managementController.getAllPatientVisitsByLBP(UUID.randomUUID().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void testSavePatientVisit() {
        Set<String> roles = new TreeSet<>();
        roles.add(Constants.VISA_MED_SESTRA);
        when(loggedInUser.getRoles()).thenReturn(roles);
        when(posetaPacijentuService.save(any(PosetaPacijentu.class))).thenAnswer(i -> new PosetaPacijentu());
        SavePatientVisitRequestDTO requestDTO = new SavePatientVisitRequestDTO();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setPatientName("a");
        requestDTO.setNote("a");
        requestDTO.setPatientPID("a");
        requestDTO.setPatientSurname("a");

        ResponseEntity<?> response = managementController.savePatientVisit(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testSavePatientVisitForbidden() {
        Set<String> roles = new TreeSet<>();
        when(loggedInUser.getRoles()).thenReturn(roles);
        SavePatientVisitRequestDTO requestDTO = new SavePatientVisitRequestDTO();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setPatientName("a");
        requestDTO.setNote("a");
        requestDTO.setPatientPID("a");
        requestDTO.setPatientSurname("a");

        ResponseEntity<?> response = managementController.savePatientVisit(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }
}
