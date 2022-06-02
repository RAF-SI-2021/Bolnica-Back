import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import raf.si.bolnica.laboratory.constants.Constants;
import raf.si.bolnica.laboratory.controllers.LaboratoryController;
import raf.si.bolnica.laboratory.interceptors.LoggedInUser;
import raf.si.bolnica.laboratory.requests.GetLabExaminationsByDateDTO;
import raf.si.bolnica.laboratory.services.*;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LaboratoryController.class)
@ContextConfiguration(classes = { LaboratoryController.class })
@WebAppConfiguration
class LaboratoryControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoggedInUser loggedInUser;
    @MockBean
    private UputService uputService;
    @MockBean
    private LaboratorijskiRadniNalogService radniNalogService;
    @MockBean
    private LaboratorijskaAnalizaService laboratorijskaAnalizaService;
    @MockBean
    private ParametarService parametarService;
    @MockBean
    private ZakazanLaboratorijskiPregledService zakazanLaboratorijskiPregledService;
    @MockBean
    private ParametarAnalizeService parametarAnalizeService;
    @MockBean
    private RezultatParametraAnalizeService rezultatParametraAnalizeService;
    @MockBean
    private EntityManager entityManager;

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    // /get-lab-examination-count - getLabExaminationsOnDate()
    @Test
    void getLabExaminationsForCurrentTime() throws Exception {
        when(loggedInUser.getRoles()).thenReturn(allRoles());
        GetLabExaminationsByDateDTO reqBody = new GetLabExaminationsByDateDTO();
        reqBody.setDateAndTime(new Timestamp(System.currentTimeMillis()));
        MvcResult result = mvc.perform(post("/get-lab-examination-count")
                .content(asJsonString(reqBody))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        assertNull(result.getResponse().getContentType());
    }

    @Test
    void getLabExaminations() {
    }

    @Test
    void setLabExaminationStatus() {
    }

    @Test
    void scheduleLabExamination() {
    }

    @Test
    void createLaboratorijskiRadniNalog() {
    }

    @Test
    void getLaboratorijskiRadniNalogIstorija() {
    }

    @Test
    void fetchRezultatiParametaraAnalize() {
    }

    @Test
    void saveRezultatParametaraAnalize() {
    }

    @Test
    void getLaboratorijskiRadniNalogPretraga() {
    }

    @Test
    void getLaboratorijskiRadniNalog() {
    }

    @Test
    void verifyLaboratorijskiRadniNalog() {
    }

    @Test
    void createUput() {
    }

    @Test
    void deleteUput() {
    }

    @Test
    void fetchUput() {
    }

    @Test
    void uputHistory() {
    }

    @Test
    void unprocessedUputi() {
    }
}
