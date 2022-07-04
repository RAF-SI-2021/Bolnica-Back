import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import raf.si.bolnica.laboratory.constants.Constants;
import raf.si.bolnica.laboratory.controllers.LaboratoryController;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogRequestDTO;
import raf.si.bolnica.laboratory.dto.request.LaboratorijskiRadniNalogSearchRequestDTO;
import raf.si.bolnica.laboratory.dto.request.RezultatParametraAnalizeSaveRequestDTO;
import raf.si.bolnica.laboratory.dto.request.UputHistoryRequestDTO;
import raf.si.bolnica.laboratory.entities.*;
import raf.si.bolnica.laboratory.entities.enums.StatusObrade;
import raf.si.bolnica.laboratory.entities.enums.TipUputa;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.services.*;
import raf.si.bolnica.laboratory.requests.CreateUputDTO;

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
public class ReferralOperationsTest{

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

    private CreateUputDTO getUputDTO() {
        CreateUputDTO uput  = new CreateUputDTO();


        uput.setLbp(UUID.randomUUID().toString());
        uput.setZahtevaneAnalize("KKS,GLU,SE");
        uput.setLbz(UUID.randomUUID().toString());
        uput.setDatumVremeKreiranja(Timestamp.valueOf(LocalDateTime.now()));
        uput.setIzOdeljenjaId(1);
        uput.setZaOdeljenjeId(1);
        uput.setTip(TipUputa.LABORATORIJA);
        return uput;
    }

    private UputHistoryRequestDTO createHistoryDto() {
        UputHistoryRequestDTO uput  = new UputHistoryRequestDTO();


        uput.setLbp(UUID.randomUUID().toString());
        uput.setDoDatuma(null);
        uput.setOdDatuma(null);
        return uput;
    }

    private Uput makeUput(long uputId) {
        Uput uput = new Uput();
        uput.setUputId(uputId);
        uput.setLbp(UUID.fromString("4fa54fea-d850-11ec-9d64-0242ac120002"));
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
    public void createUputSuccessTest() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        ResponseEntity<?> response = laboratoryController.createUput(getUputDTO());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void createUputUnauthorizedTest() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.createUput(getUputDTO());
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void createUputWithNoRequiredParams() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        CreateUputDTO CUD = getUputDTO();
        CUD.setLbz(null);
        ResponseEntity<?> response = laboratoryController.createUput(CUD);
        assertThat(response.getStatusCodeValue()).isEqualTo(406);
    }
    @Test
    public void deleteUputSuccessTest() {
        makeUput(1);
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        ResponseEntity<?> response = laboratoryController.deleteUput(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    public void deleteUputUnauthorizedTest() {
        makeUput(1);
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.deleteUput(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    public void fetchUput() {
        makeUput(1);
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        ResponseEntity<?> response = laboratoryController.fetchUput(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void fetchUputUnauthorizedTest() {
        makeUput(1);
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        ResponseEntity<?> response = laboratoryController.fetchUput(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }


    @Test
    public void uputHistoryNoParams() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT u from Uput u WHERE u.lbp = :lbp";
        TypedQuery query = mock(TypedQuery.class);
        List<Uput> lista = new ArrayList<>();
        lista.add(new Uput());
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
        UputHistoryRequestDTO requestDTO = createHistoryDto();

        ResponseEntity<?> response = laboratoryController.uputHistory(requestDTO,1,1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }


    @Test
    public void uputHistoryAllParams() {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        String s = "SELECT u from Uput u WHERE u.lbp = :lbp AND u.datumVremeKreiranja <= :do AND u.datumVremeKreiranja >= :od";
        TypedQuery query = mock(TypedQuery.class);
        List<Uput> lista = new ArrayList<>();
        lista.add(new Uput());
        when(query.getResultList()).thenReturn(lista);
        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
        UputHistoryRequestDTO requestDTO = createHistoryDto();
        requestDTO.setDoDatuma(Timestamp.valueOf(LocalDateTime.now()));
        requestDTO.setOdDatuma(Timestamp.valueOf(LocalDateTime.now()));
        ResponseEntity<?> response = laboratoryController.uputHistory(requestDTO,1,1);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void unproccesedUputiUnauthorized() {
        when(loggedInUser.getRoles()).thenReturn(noRoles());
        UUID lbp  = UUID.fromString("4fa54fea-d850-11ec-9d64-0242ac120002");
        ResponseEntity<?> response = laboratoryController.unprocessedUputi(lbp.toString());
        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

//    @Test
//    public void unproccesedUputiSuccess() {
//        when(loggedInUser.getRoles()).thenReturn(allRoles());
//        String s = "SELECT u from Uput u WHERE u.lbp = :lbp AND u.status = :status AND u.uputId NOT IN (SELECT uput.uputId from LaboratorijskiRadniNalog) ";
//        TypedQuery query = mock(TypedQuery.class);
//        List<Uput> lista = new ArrayList<>();
//        lista.add(new Uput());
//        when(query.getResultList()).thenReturn(lista);
//        when(entityManager.createQuery(eq(s),any(Class.class))).thenReturn(query);
//        makeUput(1);
//        UUID lbp  = UUID.fromString("4fa54fea-d850-11ec-9d64-0242ac120002");
//        ResponseEntity<?> response = laboratoryController.unprocessedUputi(lbp.toString());
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//    }

}
