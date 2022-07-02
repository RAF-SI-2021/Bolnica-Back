import com.google.gson.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.bolnica.laboratory.LaboratoryApplication;
import raf.si.bolnica.laboratory.configuration.SpringWebConfiguration;
import raf.si.bolnica.laboratory.jwt.JwtProperties;

import javax.servlet.ServletContext;
import java.lang.reflect.Type;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringWebConfiguration.class, JwtProperties.class})
@SpringBootTest(classes = LaboratoryApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LaboratoryControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public String jwt;

    private Gson g;

    @BeforeEach
    public void prepareTest() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @BeforeAll
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        g = builder.create();

        // Jwt has to be used for all methods. For example:

        // jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsIm5hbWUiOiJhZG1pbiIsInN1cm5hbWUiOiJhZG1pbmljIiwidGl0bGUiOiJ0aXR1bGEiLCJwcm9mZXNzaW9uIjoiemFuaW1hbmplIiwiTEJaIjoiMjhhYWI4NmMtZDQwZi00ZTZmLTljZDEtN2RhNTU3Nzk4NzdmIiwiUEJPIjoxMjM0NSwiZGVwYXJ0bWVudCI6IkhpcnVyZ2lqYSIsIlBCQiI6MTIzNCwiaG9zcGl0YWwiOiJLbGluacSNa28tYm9sbmnEjWtpIGNlbnRhciBcIkRyYWdpxaFhIE1pxaFvdmnEh1wiIiwicm9sZXMiOiJST0xFX0RSX1NQRUNfUE9WLFJPTEVfTUVEX1NFU1RSQSxST0xFX0RSX1NQRUMsUk9MRV9NRURJQ0lOU0tJX0JJT0hFTUlDQVIsUk9MRV9EUl9TUEVDX09ERUxKRU5KQSxST0xFX1ZJU0lfTEFCT1JBVE9SSUpTS0lfVEVITklDQVIsUk9MRV9SRUNFUENJT05FUixST0xFX0FETUlOLFJPTEVfU1BFQ0lKQUxJU1RBX01FRElDSU5TS0VfQklPSEVNSUpFLFJPTEVfVklTQV9NRURfU0VTVFJBLFJPTEVfTEFCT1JBVE9SSUpTS0lfVEVITklDQVIiLCJpc3MiOiJRbnVRYmxRV244SDlnZ2l3ZkdiQ3hwUEEzZ2RZMW9BZSIsImV4cCI6MTY1NjkyMTM4OH0.a_tIzTRuKuq_5oqUDO5ygParQXcg5ZBra03HFwREdVs";

        // TODO
        //  smislliti - vrv bolje za sad jwt='blabla'
        //  ne moze na ovaj nacin za sada da se gadja endpoint drugog servisa (login)

//        ResultActions resultActions = mockMvc.perform(post("http://localhost:" + 8081 + "/api/login")
//                        .contentType("application/json")
//                        .content("{\n" +
//                                "    \"email\": \"test@gmail.com\",\n" +
//                                "    \"password\": \"superadmin\"\n" +
//                                "}"))
//                .andExpect(status().isOk());
//
//        MvcResult mvcResult = resultActions.andReturn();
//        System.out.println(mvcResult.getResponse().getContentAsString());
//        jwt = mvcResult.getResponse().getContentAsString().replace("\"", "");

    }

    @AfterAll
    public void cleanup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    }


    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        final ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("laboratoryController"));
    }


    @Test
    void getLabExaminationsOnDate() {
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
    void setUputStatus() {
    }

    @Test
    void uputHistory() {
    }

    @Test
    void unprocessedUputi() {
    }

    @Test
    void unprocessedUputiWithType() {
    }
}
