import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.laboratory.constants.Constants;
import raf.si.bolnica.laboratory.controllers.LaboratoryController;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogRequestDTO;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogSearchRequestDTO;
import raf.si.bolnica.laboratory.dto.request.RezultatParametraAnalizeSaveRequestDTO;
import raf.si.bolnica.laboratory.dto.response.LaboratorijskiRadniNalogResponseDTO;
import raf.si.bolnica.laboratory.entities.*;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;
import raf.si.bolnica.laboratory.entities.enums.StatusUputa;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.requests.*;
import raf.si.bolnica.laboratory.services.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LaboratoryWorkOrdersTests {

    @Mock
    private LoggedInUser loggedInUser;

    @Mock
    private UputService uputService;

    @Mock
    private LaboratorijskiRadniNalogService radniNalogService;

    @Mock
    private LaboratorijskaAnalizaService laboratorijskaAnalizaService;

    @Mock
    private ParametarAnalizeService parametarAnalizeService;

    @Mock
    private RezultatParametraAnalizeService rezultatParametraAnalizeService;

    @Mock
    private ZakazanLaboratorijskiPregledService zakazanLaboratorijskiPregledService;

    @Mock
    private PdfGeneratorService pdfGeneratorService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LaboratoryController laboratoryController;

    private void addAnalysis() {
        LaboratorijskaAnaliza laboratorijskaAnaliza = new LaboratorijskaAnaliza();
        List<LaboratorijskaAnaliza> analizaList = new ArrayList<>();
        analizaList.add(laboratorijskaAnaliza);
        ParametarAnalize parametar = new ParametarAnalize();
        List<ParametarAnalize> parametarList = new ArrayList<>();
        parametarList.add(parametar);
        when(laboratorijskaAnalizaService.getLaboratorijskaAnalizaBySkracenica(any(String.class))).thenReturn(analizaList);
        when(parametarAnalizeService.getParametarAnalizeByLaboratorijskaAnaliza(any(LaboratorijskaAnaliza.class))).thenReturn(parametarList);
    }

    private Uput getUput(long uputId) {
        Uput uput = new Uput();
        uput.setUputId(uputId);
        uput.setLbp(UUID.randomUUID());
        uput.setZahtevaneAnalize("KKS,GLU,SE");
        uput.setLbz(UUID.randomUUID());
        uput.setDatumVremeKreiranja(Timestamp.valueOf(LocalDateTime.now()));
        uput.setIzOdeljenjaId(1);
        uput.setZaOdeljenjeId(1);
        uput.setTip(TipUputa.LABORATORIJA);
        return uput;
    }

    private ZakazanLaboratorijskiPregled getZakazanLaboratorijskiPregled() {
        ZakazanLaboratorijskiPregled zakazanLaboratorijskiPregled = new ZakazanLaboratorijskiPregled();
        return zakazanLaboratorijskiPregled;
    }

    private LaboratorijskiRadniNalog getRadniNalog(long id) {
        LaboratorijskiRadniNalog laboratorijskiRadniNalog = new LaboratorijskiRadniNalog();
        laboratorijskiRadniNalog.setLaboratorijskiRadniNalogId(id);
        laboratorijskiRadniNalog.setLbzBiohemicar(UUID.randomUUID());
        laboratorijskiRadniNalog.setLbzTehnicar(UUID.randomUUID());
        laboratorijskiRadniNalog.setLbp(UUID.randomUUID());
        laboratorijskiRadniNalog.setStatusObrade(StatusObrade.NEOBRADJEN);
        laboratorijskiRadniNalog.setDatumVremeKreiranja(Timestamp.valueOf(LocalDateTime.now()));
        laboratorijskiRadniNalog.setUput(getUput(0));
        return laboratorijskiRadniNalog;

    }

    private Set<String> allRoles() {
        Set<String> roles = new HashSet<>();
        roles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        roles.add(Constants.LABORATORIJSKI_TEHNICAR);
        roles.add(Constants.SPEC_BIOHEMICAR);
        roles.add(Constants.BIOHEMICAR);
        roles.add(Constants.DR_SPEC_POV);
        roles.add(Constants.DR_SPEC);
        roles.add(Constants.NACELNIK_ODELJENJA);
        roles.add(Constants.VISA_MED_SESTRA);
        roles.add(Constants.MED_SESTRA);
        return roles;
    }

    private Set<String> noRoles() {
        Set<String> roles = new HashSet<>();
        return roles;
    }

    @Test
    public void createNalogUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.createLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void createNalogNoUputTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        ResponseEntity<?> response = laboratoryController.createLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }

    @Test
    public void createNalogSuccessTest() {
        addAnalysis();
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(uputService.fetchUputById(any(Long.class))).thenAnswer(i -> getUput((Long) i.getArguments()[0]));
        when(radniNalogService.saveRadniNalog(any(LaboratorijskiRadniNalog.class))).thenAnswer(i -> getRadniNalog(0));
        ResponseEntity<?> response = laboratoryController.createLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        LaboratorijskiRadniNalogResponseDTO laboratorijskiRadniNalogDto = (LaboratorijskiRadniNalogResponseDTO) response.getBody();
        assert laboratorijskiRadniNalogDto != null;
    }

    @Test
    public void getLabExaminationCountForbidden() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        GetLabExaminationsByDateDTO getLabExaminationsByDateDTO = new GetLabExaminationsByDateDTO();
        getLabExaminationsByDateDTO.setDateAndTime(new Timestamp(1000));
        ResponseEntity<?> response = laboratoryController.getLabExaminationsOnDate(getLabExaminationsByDateDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getLabExaminationCountSuccess() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(zakazanLaboratorijskiPregledService.getZakazaniPreglediByDate(any(java.sql.Date.class))).thenAnswer(i -> new ArrayList<>());
        GetLabExaminationsByDateDTO getLabExaminationsByDateDTO = new GetLabExaminationsByDateDTO();
        getLabExaminationsByDateDTO.setDateAndTime(new Timestamp(1000));
        ResponseEntity<?> response = laboratoryController.getLabExaminationsOnDate(getLabExaminationsByDateDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getLabExaminationsForbidden() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        FindScheduledLabExaminationsDTO findScheduledLabExaminationsDTO = new FindScheduledLabExaminationsDTO();
        findScheduledLabExaminationsDTO.setDate(new Date(1000));
        findScheduledLabExaminationsDTO.setLbp(UUID.randomUUID().toString());
        ResponseEntity<?> response = laboratoryController.getLabExaminations(findScheduledLabExaminationsDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getLabExaminations1Success() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndZakazanDatumAndLbp(any(Integer.class), any(java.sql.Date.class), any(UUID.class))).thenAnswer(i -> new ArrayList<>());
        FindScheduledLabExaminationsDTO findScheduledLabExaminationsDTO = new FindScheduledLabExaminationsDTO();
        findScheduledLabExaminationsDTO.setDate(new Date(1000));
        findScheduledLabExaminationsDTO.setLbp(UUID.randomUUID().toString());
        ResponseEntity<?> response = laboratoryController.getLabExaminations(findScheduledLabExaminationsDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getLabExaminations2Success() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndLbp(any(Integer.class), any(UUID.class))).thenAnswer(i -> new ArrayList<>());
        FindScheduledLabExaminationsDTO findScheduledLabExaminationsDTO = new FindScheduledLabExaminationsDTO();
        findScheduledLabExaminationsDTO.setLbp(UUID.randomUUID().toString());
        ResponseEntity<?> response = laboratoryController.getLabExaminations(findScheduledLabExaminationsDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getLabExaminations3Success() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(zakazanLaboratorijskiPregledService.findByOdeljenjeIdAndZakazanDatum(any(Integer.class), any(java.sql.Date.class))).thenAnswer(i -> new ArrayList<>());
        FindScheduledLabExaminationsDTO findScheduledLabExaminationsDTO = new FindScheduledLabExaminationsDTO();
        findScheduledLabExaminationsDTO.setDate(new Date(1000));
        ResponseEntity<?> response = laboratoryController.getLabExaminations(findScheduledLabExaminationsDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void setLabExaminationStatusSuccess() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(zakazanLaboratorijskiPregledService.getZakazanPregled(any(Long.class))).thenAnswer(i -> new ZakazanLaboratorijskiPregled());
        SetStatusExaminationDTO setStatusExaminationDTO = new SetStatusExaminationDTO();
        setStatusExaminationDTO.setStatus("ZAKAZANO");
        setStatusExaminationDTO.setId(1);
        ResponseEntity<?> response = laboratoryController.setLabExaminationStatus(setStatusExaminationDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void setLabExaminationStatusForbidden() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        SetStatusExaminationDTO setStatusExaminationDTO = new SetStatusExaminationDTO();
        setStatusExaminationDTO.setStatus("ZAKAZANO");
        setStatusExaminationDTO.setId(1);
        ResponseEntity<?> response = laboratoryController.setLabExaminationStatus(setStatusExaminationDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void scheduleLabExaminationForbidden() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ScheduleLabExaminationDTO request = new ScheduleLabExaminationDTO();
        request.setDate(new Date(10000));
        request.setLbp(UUID.randomUUID().toString());
        request.setNapomena("");
        ResponseEntity<?> response = laboratoryController.scheduleLabExamination(request);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void scheduleLabExaminationSuccessTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(zakazanLaboratorijskiPregledService.saveZakazanPregled(any(ZakazanLaboratorijskiPregled.class))).thenAnswer(i -> getZakazanLaboratorijskiPregled());
        ScheduleLabExaminationDTO request = new ScheduleLabExaminationDTO();
        request.setDate(new Date(10000));
        request.setLbp(UUID.randomUUID().toString());
        request.setNapomena("");
        ResponseEntity<?> response = laboratoryController.scheduleLabExamination(request);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        ZakazanLaboratorijskiPregled zakazanLaboratorijskiPregled = (ZakazanLaboratorijskiPregled) response.getBody();
        assert zakazanLaboratorijskiPregled != null;
    }

    @Test
    public void getLaboratorijskiRadniNalogSuccess() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        LaboratorijskiRadniNalog laboratorijskiRadniNalog = new LaboratorijskiRadniNalog();
        Uput uput = new Uput();
        uput.setUputId(1);
        laboratorijskiRadniNalog.setUput(uput);
        when(radniNalogService.fetchRadniNalogById(any(Long.class))).thenAnswer(i -> laboratorijskiRadniNalog);
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getLaboratorijskiRadniNalogForbidden() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void setUputStatusForbidden() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        SetUputStatusDTO setUputStatusDTO = new SetUputStatusDTO();
        setUputStatusDTO.setStatusUputa(StatusUputa.REALIZOVAN);
        setUputStatusDTO.setUputId(1);

        ResponseEntity<?> response = laboratoryController.setUputStatus(setUputStatusDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void unprocessedUputiWithTypeSuccess() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> mock(TypedQuery.class));
        SearchUputiDTO setUputStatusDTO = new SearchUputiDTO();
        setUputStatusDTO.setLbp(UUID.randomUUID().toString());
        setUputStatusDTO.setTipUputa(TipUputa.LABORATORIJA);
        ResponseEntity<?> response = laboratoryController.unprocessedUputiWithType(setUputStatusDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void labReportTest() {
        when(pdfGeneratorService.reportPrint(any(Map.class))).thenAnswer(i -> new ResponseEntity<Void>(HttpStatus.OK));
        laboratoryController.labReportPrint(new HashMap<>());
    }

    @Test
    public void unprocessedUputiWithTypeForbidden() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        SearchUputiDTO setUputStatusDTO = new SearchUputiDTO();
        setUputStatusDTO.setLbp(UUID.randomUUID().toString());
        setUputStatusDTO.setTipUputa(TipUputa.LABORATORIJA);
        ResponseEntity<?> response = laboratoryController.unprocessedUputiWithType(setUputStatusDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void setUputStatusSuccess() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        when(uputService.fetchUputById(any(Long.class))).thenAnswer(i -> getUput(1));
        SetUputStatusDTO setUputStatusDTO = new SetUputStatusDTO();
        setUputStatusDTO.setStatusUputa(StatusUputa.REALIZOVAN);
        setUputStatusDTO.setUputId(1);

        ResponseEntity<?> response = laboratoryController.setUputStatus(setUputStatusDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getHistoryUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogIstorija(new LaboratorijskiRadniNalogRequestDTO(), 1, 1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getHistoryNoParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE (l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.U_OBRADI OR l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN)";
        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(eq(s), any(Class.class))).thenReturn(query);
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogIstorija(new LaboratorijskiRadniNalogRequestDTO(), 1, 1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getHistoryAllParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE (l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.U_OBRADI OR l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN) AND l.lbp = :lbp AND l.datumVremeKreiranja >= :od AND l.datumVremeKreiranja <= :do";
        TypedQuery query = mock(TypedQuery.class);
        List<LaboratorijskiRadniNalog> lista = new ArrayList<>();
        LaboratorijskiRadniNalog laboratorijskiRadniNalog = new LaboratorijskiRadniNalog();
        Uput uput = new Uput();
        uput.setUputId(1);
        laboratorijskiRadniNalog.setUput(uput);
        lista.add(laboratorijskiRadniNalog);
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s), any(Class.class))).thenReturn(query);
        LaboratorijskiRadniNalogRequestDTO requestDTO = new LaboratorijskiRadniNalogRequestDTO();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setOdDatuma(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setDoDatuma(Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogIstorija(requestDTO, 1, 1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void fetchRezultatiUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.fetchRezultatiParametaraAnalize(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void fetchRezultatiBiohemicarTest() {
        Set<String> roles = noRoles();
        roles.add(Constants.BIOHEMICAR);
        when(loggedInUser.getRoles()).thenReturn(roles);
        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE l.laboratorijskiRadniNalogId = :id";
        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(eq(s), any(Class.class))).thenReturn(query);
        ResponseEntity<?> response = laboratoryController.fetchRezultatiParametaraAnalize(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }

    @Test
    public void fetchRezultatiSuccessTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s1 = "SELECT l from LaboratorijskiRadniNalog l WHERE l.laboratorijskiRadniNalogId = :id AND l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN";
        TypedQuery query1 = mock(TypedQuery.class);
        ArrayList<LaboratorijskiRadniNalog> list1 = new ArrayList<>();
        LaboratorijskiRadniNalog laboratorijskiRadniNalog = new LaboratorijskiRadniNalog();
        Uput uput = new Uput();
        uput.setUputId(1);
        laboratorijskiRadniNalog.setUput(uput);
        list1.add(laboratorijskiRadniNalog);

        String s2 = "SELECT r from RezultatParametraAnalize r WHERE r.laboratorijskiRadniNalog = :nalog";
        TypedQuery query2 = mock(TypedQuery.class);
        ArrayList<RezultatParametraAnalize> list2 = new ArrayList<>();
        RezultatParametraAnalize rezultat = new RezultatParametraAnalize();
        ParametarAnalize parametarAnalize = new ParametarAnalize();
        parametarAnalize.setParametar(new Parametar());
        parametarAnalize.setLaboratorijskaAnaliza(new LaboratorijskaAnaliza());
        rezultat.setParametarAnalize(parametarAnalize);
        list2.add(rezultat);

        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            if(i.getArguments()[0].equals(s1)) return query1;
            else if(i.getArguments()[0].equals(s2)) return query2;
            else return mock(TypedQuery.class);
        });
        ResponseEntity<?> response = laboratoryController.fetchRezultatiParametaraAnalize(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }

    @Test
    public void saveRezultatiUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.saveRezultatParametaraAnalize(new RezultatParametraAnalizeSaveRequestDTO());
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void saveRezultatiNeobradjenNalogTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        RezultatParametraAnalizeSaveRequestDTO requestDTO = new RezultatParametraAnalizeSaveRequestDTO();
        LaboratorijskiRadniNalog nalog = new LaboratorijskiRadniNalog();
        nalog.setStatusObrade(StatusObrade.NEOBRADJEN);
        requestDTO.setNalogId(1);
        requestDTO.setParametarId(1);
        requestDTO.setRezultat("42");
        when(radniNalogService.fetchRadniNalogById(eq(1L))).thenReturn(nalog);
        ResponseEntity<?> response = laboratoryController.saveRezultatParametaraAnalize(requestDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }

    @Test
    public void saveRezultatiSuccessTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        RezultatParametraAnalizeSaveRequestDTO requestDTO = new RezultatParametraAnalizeSaveRequestDTO();
        LaboratorijskiRadniNalog nalog = new LaboratorijskiRadniNalog();
        nalog.setStatusObrade(StatusObrade.OBRADJEN);
        requestDTO.setNalogId(1);
        requestDTO.setParametarId(1);
        requestDTO.setRezultat("42");
        when(radniNalogService.fetchRadniNalogById(eq(1L))).thenReturn(nalog);
        when(rezultatParametraAnalizeService.getRezultatParametraAnalize(any(RezultatParametraAnalizeKey.class))).thenReturn(new RezultatParametraAnalize());
        ResponseEntity<?> response = laboratoryController.saveRezultatParametaraAnalize(requestDTO);
        assertThat(response.getStatusCodeValue()).isEqualTo(202);
    }

    @Test
    public void fetchOrdersUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        LaboratorijskiRadniNalogSearchRequestDTO requestDTO = new LaboratorijskiRadniNalogSearchRequestDTO();
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogPretraga(requestDTO, 1, 1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void fetchOrdersNoParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT lrn FROM LaboratorijskiRadniNalog lrn";
        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(eq(s), any(Class.class))).thenReturn(query);
        LaboratorijskiRadniNalogSearchRequestDTO requestDTO = new LaboratorijskiRadniNalogSearchRequestDTO();
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogPretraga(requestDTO, 1, 1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void fetchOrdersAllParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT lrn FROM LaboratorijskiRadniNalog lrn WHERE lrn.lbp = :lbp AND lrn.datumVremeKreiranja <= :do AND lrn.statusObrade = :status AND lrn.datumVremeKreiranja >= :od";
        TypedQuery query = mock(TypedQuery.class);
        List<LaboratorijskiRadniNalog> lista = new ArrayList<>();
        LaboratorijskiRadniNalog laboratorijskiRadniNalog = new LaboratorijskiRadniNalog();
        Uput uput = new Uput();
        uput.setUputId(1);
        laboratorijskiRadniNalog.setUput(uput);
        lista.add(laboratorijskiRadniNalog);
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s), any(Class.class))).thenReturn(query);
        LaboratorijskiRadniNalogSearchRequestDTO requestDTO = new LaboratorijskiRadniNalogSearchRequestDTO();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setOdDatuma(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setDoDatuma(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setStatusObrade(StatusObrade.NEOBRADJEN);
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogPretraga(requestDTO, 1, 1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void verifyUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.verifyLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void verifyOKTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        LaboratorijskiRadniNalog nalog = new LaboratorijskiRadniNalog();
        nalog.setUput(new Uput());
        when(radniNalogService.fetchRadniNalogById(any(Long.class))).thenReturn(nalog);

        String s = "SELECT r from RezultatParametraAnalize r WHERE r.laboratorijskiRadniNalog = :nalog";
        TypedQuery query = mock(TypedQuery.class);
        List<RezultatParametraAnalize> lista = new ArrayList<>();
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s), any(Class.class))).thenReturn(query);

        ResponseEntity<?> response = laboratoryController.verifyLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(202);
    }

    @Test
    public void verifyNotOKTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        LaboratorijskiRadniNalog nalog = new LaboratorijskiRadniNalog();
        nalog.setUput(new Uput());
        when(radniNalogService.fetchRadniNalogById(any(Long.class))).thenReturn(nalog);

        String s = "SELECT r from RezultatParametraAnalize r WHERE r.laboratorijskiRadniNalog = :nalog";
        TypedQuery query = mock(TypedQuery.class);
        List<RezultatParametraAnalize> lista = new ArrayList<>();
        RezultatParametraAnalize rezultat1 = new RezultatParametraAnalize();
        rezultat1.setRezultat("rez");
        RezultatParametraAnalize rezultat2 = new RezultatParametraAnalize();
        ParametarAnalize parametarAnalize = new ParametarAnalize();
        LaboratorijskaAnaliza laboratorijskaAnaliza = new LaboratorijskaAnaliza();
        laboratorijskaAnaliza.setNazivAnalize("naziv");
        parametarAnalize.setLaboratorijskaAnaliza(laboratorijskaAnaliza);
        rezultat2.setParametarAnalize(parametarAnalize);
        lista.add(rezultat1);
        lista.add(rezultat2);
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s), any(Class.class))).thenReturn(query);

        ResponseEntity<?> response = laboratoryController.verifyLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }


}
