import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import raf.si.bolnica.laboratory.constants.Constants;
import raf.si.bolnica.laboratory.controllers.LaboratoryController;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogRequestDTO;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogSearchRequestDTO;
import raf.si.bolnica.laboratory.dto.request.RezultatParametraAnalizeSaveRequestDTO;
import raf.si.bolnica.laboratory.entities.*;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.services.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
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

    private Set<String> allRoles() {
        Set<String> roles = new HashSet<>();
        roles.add(Constants.VISI_LABORATORIJSKI_TEHNICAR);
        roles.add(Constants.LABORATORIJSKI_TEHNICAR);
        roles.add(Constants.SPEC_BIOHEMICAR);
        roles.add(Constants.BIOHEMICAR);
        roles.add(Constants.DR_SPEC_POV);
        roles.add(Constants.DR_SPEC);
        roles.add(Constants.NACELNIK_ODELJENJA);
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
        when(uputService.fetchUputById(any(Long.class))).thenAnswer(i -> getUput((Long)i.getArguments()[0]));
        ResponseEntity<?> response = laboratoryController.createLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(202);
    }

    @Test
    public void getHistoryUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogIstorija(new LaboratorijskiRadniNalogRequestDTO(),1,1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void getHistoryNoParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE (l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.U_OBRADI OR l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN)";
        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogIstorija(new LaboratorijskiRadniNalogRequestDTO(),1,1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getHistoryAllParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT l from LaboratorijskiRadniNalog l WHERE (l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.U_OBRADI OR l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN) AND l.lbp = :lbp AND l.datumVremeKreiranja >= :od AND l.datumVremeKreiranja <= :do";
        TypedQuery query = mock(TypedQuery.class);
        List<LaboratorijskiRadniNalog> lista = new ArrayList<>();
        lista.add(new LaboratorijskiRadniNalog());
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
        LaboratorijskiRadniNalogRequestDTO requestDTO = new LaboratorijskiRadniNalogRequestDTO();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setOdDatuma(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setDoDatuma(Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogIstorija(requestDTO,1,1);
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
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
        ResponseEntity<?> response = laboratoryController.fetchRezultatiParametaraAnalize(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }

    @Test
    public void fetchRezultatiSuccessTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s1 = "SELECT l from LaboratorijskiRadniNalog l WHERE l.laboratorijskiRadniNalogId = :id AND l.statusObrade = raf.si.bolnica.laboratory.entities.enums.StatusObrade.OBRADJEN";
        TypedQuery query1 = mock(TypedQuery.class);
        ArrayList<LaboratorijskiRadniNalog> list1 = new ArrayList<>();
        list1.add(new LaboratorijskiRadniNalog());
        when(query1.getResultList()).thenReturn(list1);

        String s2 = "SELECT r from RezultatParametraAnalize r WHERE r.laboratorijskiRadniNalog = :nalog";
        TypedQuery query2 = mock(TypedQuery.class);
        ArrayList<RezultatParametraAnalize> list2 = new ArrayList<>();
        RezultatParametraAnalize rezultat = new RezultatParametraAnalize();
        ParametarAnalize parametarAnalize = new ParametarAnalize();
        parametarAnalize.setParametar(new Parametar());
        parametarAnalize.setLaboratorijskaAnaliza(new LaboratorijskaAnaliza());
        rezultat.setParametarAnalize(parametarAnalize);
        list2.add(rezultat);
        when(query2.getResultList()).thenReturn(list2);

        when(entityManager.createQuery(any(String.class),any(Class.class))).thenAnswer(i -> {
            if(i.getArguments()[0].equals(s1)) return query1;
            else if(i.getArguments()[0].equals(s2)) return query2;
            else return null;
        });
        ResponseEntity<?> response = laboratoryController.fetchRezultatiParametaraAnalize(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
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
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogPretraga(requestDTO,1,1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void fetchOrdersNoParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT lrn FROM LaboratorijskiRadniNalog lrn INNER JOIN lrn.uput u WHERE u.zaOdeljenjeId = :lab";
        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
        LaboratorijskiRadniNalogSearchRequestDTO requestDTO = new LaboratorijskiRadniNalogSearchRequestDTO();
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogPretraga(requestDTO,1,1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void fetchOrdersAllParamsTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT lrn FROM LaboratorijskiRadniNalog lrn INNER JOIN lrn.uput u WHERE u.zaOdeljenjeId = :lab AND lrn.lbp = :lbp AND lrn.datumVremeKreiranja <= :do AND lrn.statusObrade = :status AND lrn.datumVremeKreiranja >= :od";
        TypedQuery query = mock(TypedQuery.class);
        List<LaboratorijskiRadniNalog> lista = new ArrayList<>();
        lista.add(new LaboratorijskiRadniNalog());
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
        LaboratorijskiRadniNalogSearchRequestDTO requestDTO = new LaboratorijskiRadniNalogSearchRequestDTO();
        requestDTO.setLbp(UUID.randomUUID().toString());
        requestDTO.setOdDatuma(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setDoDatuma(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setStatusObrade(StatusObrade.NEOBRADJEN);
        ResponseEntity<?> response = laboratoryController.getLaboratorijskiRadniNalogPretraga(requestDTO,1,1);
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
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);

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
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);

        ResponseEntity<?> response = laboratoryController.verifyLaboratorijskiRadniNalog(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }



}
