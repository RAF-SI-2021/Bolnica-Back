import com.google.gson.*;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import raf.si.bolnica.laboratory.LaboratoryApplication;
import raf.si.bolnica.laboratory.configuration.SpringWebConfiguration;
import raf.si.bolnica.laboratory.jwt.JwtProperties;

import javax.servlet.ServletContext;
import java.lang.reflect.Type;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject personJsonObject = new JSONObject();
        personJsonObject.put("email", "test@gmail.com");
        personJsonObject.put("password", "superadmin");
        HttpEntity<String> entity = new HttpEntity<>(personJsonObject.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8081/api/login", HttpMethod.POST, entity, String.class);
        jwt = response.getBody().replace("\"", "");

        mockMvc.perform(post("/api/schedule-lab-examination")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"lbp\" : \"237e9877-e79b-12d4-a765-321741963000\",\n" + // licni br pacijenta
                        "    \"date\" : \"2022-07-01\", \n" +
                        "    \"napomena\": \"test_napomena\"\n" +
                        "}")
        ).andExpect(status().isOk());
        /*
         * --- CREATE TEST UPUT ---
         */
        mockMvc.perform(post("/api/create-uput")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"tip\" : \"LABORATORIJA\",\n" +
                        "    \"lbz\" : \"6cfe71bb-e4ee-49dd-a3ad-28e043f8b435\",\n" + // licni br zaposlenog
                        "    \"izOdeljenjaId\" : 1,\n" +
                        "    \"zaOdeljenjeId\" : 2,\n" +
                        "    \"lbp\" : \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "    \"datumVremeKreiranja\" : 1656655200000,\n" +
                        "    \"zahtevaneAnalize\" : \"GLU,HOL\"\n" +
                        "}")
        ).andExpect(status().isOk());
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
    void getLabExaminationsOnDate() throws Exception {
        mockMvc.perform(post("/api/get-lab-examination-count")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"dateAndTime\" : \"2022-07-01\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void getLabExaminations() throws Exception {
        mockMvc.perform(post("/api/get-lab-examinations")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"lbp\" : \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                                "    \"date\" : \"2022-07-01\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zakazanLaboratorijskiPregledId").value("1"))
                .andExpect(jsonPath("$[0].odeljenjeId").value("12345"))
                .andExpect(jsonPath("$[0].lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("$[0].zakazanDatum").exists())
                .andExpect(jsonPath("$[0].statusPregleda").value("OTKAZANO"))
                .andExpect(jsonPath("$[0].napomena").value("test_napomena"))
                .andExpect(jsonPath("$[0].lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"));
    }

    @Test
    void setLabExaminationStatus() throws Exception {
        mockMvc.perform(put("/api/set-lab-examination-status")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\" : 1,\n" +
                                "    \"status\": \"OTKAZANO\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("zakazanLaboratorijskiPregledId").value("1"))
                .andExpect(jsonPath("odeljenjeId").value("12345"))
                .andExpect(jsonPath("lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("zakazanDatum").exists())
                .andExpect(jsonPath("statusPregleda").value("OTKAZANO"))
                .andExpect(jsonPath("napomena").value("test_napomena"))
                .andExpect(jsonPath("lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"));
    }

    @Test
    void scheduleLabExamination() throws Exception {
        mockMvc.perform(post("/api/schedule-lab-examination")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"lbp\" : \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                                "    \"date\" : \"2022-07-01\", \n" +
                                "    \"napomena\": \"schedule_napomena\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("zakazanLaboratorijskiPregledId").value("2"))
                .andExpect(jsonPath("odeljenjeId").value("12345"))
                .andExpect(jsonPath("lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("zakazanDatum").exists())
                .andExpect(jsonPath("statusPregleda").value("ZAKAZANO"))
                .andExpect(jsonPath("napomena").value("schedule_napomena"))
                .andExpect(jsonPath("lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"));
    }

    @Test
    void createLaboratorijskiRadniNalog() throws Exception {
        /*
         *  --- CREATE TEST LAB RADNI NALOG ---
         */
        mockMvc.perform(post("/api/create-laboratory-work-order")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .param("uputId", "1")
        ).andExpect(status().isOk());
        /*
         *  --- SAVE LAB RESULT FOR NALOG ---
         */
        mockMvc.perform(put("/api/save-analysis-result")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nalogId\" : 1,\n" +
                        "    \"parametarId\" : 1,\n" +
                        "    \"rezultat\": \"5.3\"\n" +
                        "}")
        ).andExpect(status().isAccepted());

        mockMvc.perform(post("/api/create-laboratory-work-order")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("uputId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("laboratorijskiRadniNalogId").value("2"))
                .andExpect(jsonPath("datumVremeKreiranja").exists())
                .andExpect(jsonPath("statusObrade").value("NEOBRADJEN"))
                .andExpect(jsonPath("lbzTehnicar").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("lbzBiohemicar").doesNotExist())
                .andExpect(jsonPath("uput.uputId").value("1"))
                .andExpect(jsonPath("uput.tip").value("LABORATORIJA"))
                .andExpect(jsonPath("uput.lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("uput.izOdeljenjaId").value("1"))
                .andExpect(jsonPath("uput.zaOdeljenjeId").value("2"))
                .andExpect(jsonPath("uput.lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("uput.datumVremeKreiranja").exists())
                .andExpect(jsonPath("uput.status").value("REALIZOVAN"))
                .andExpect(jsonPath("uput.zahtevaneAnalize").value("GLU,HOL"))
                .andExpect(jsonPath("uput.komentar").doesNotExist())
                .andExpect(jsonPath("uput.uputnaDijagnoza").doesNotExist())
                .andExpect(jsonPath("uput.razlogUpucivanja").doesNotExist())
                .andExpect(jsonPath("rezultati").exists());
    }

    @Test
    void saveRezultatParametaraAnalize() throws Exception {
        /*
         *  --- CREATE TEST LAB RADNI NALOG ---
         */
        mockMvc.perform(post("/api/create-laboratory-work-order")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .param("uputId", "1")
        ).andExpect(status().isOk());

        mockMvc.perform(put("/api/save-analysis-result")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"nalogId\" : 1,\n" +
                                "    \"parametarId\" : 1,\n" +
                                "    \"rezultat\" : \"1.0\"\n" +
                                "}"))
                .andExpect(status().isAccepted());
    }

    @Test
    void getLaboratorijskiRadniNalog() throws Exception {
        /*
         *  --- CREATE TEST LAB RADNI NALOG ---
         */
        mockMvc.perform(post("/api/create-laboratory-work-order")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .param("uputId", "1")
        ).andExpect(status().isOk());

        mockMvc.perform(get("/api/get-work-order")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("laboratorijskiRadniNalogId").value("1"))
                .andExpect(jsonPath("datumVremeKreiranja").exists())
                .andExpect(jsonPath("statusObrade").value("OBRADJEN"))
                .andExpect(jsonPath("lbzTehnicar").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("lbzBiohemicar").exists())
                .andExpect(jsonPath("uput.uputId").value("1"))
                .andExpect(jsonPath("uput.tip").value("LABORATORIJA"))
                .andExpect(jsonPath("uput.lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("uput.izOdeljenjaId").value("1"))
                .andExpect(jsonPath("uput.zaOdeljenjeId").value("2"))
                .andExpect(jsonPath("uput.lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("uput.datumVremeKreiranja").exists())
                .andExpect(jsonPath("uput.status").exists())
                .andExpect(jsonPath("uput.zahtevaneAnalize").value("GLU,HOL"))
                .andExpect(jsonPath("uput.komentar").doesNotExist())
                .andExpect(jsonPath("uput.uputnaDijagnoza").doesNotExist())
                .andExpect(jsonPath("uput.razlogUpucivanja").doesNotExist())
                .andExpect(jsonPath("rezultati").exists());
    }

    @Test
    void verifyLaboratorijskiRadniNalog() throws Exception {
        /*
         *  --- CREATE TEST LAB RADNI NALOG ---
         */
        mockMvc.perform(post("/api/create-laboratory-work-order")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .param("uputId", "1")
        ).andExpect(status().isOk());
        /*
         *  --- SAVE FIRST LAB RESULT FOR NALOG ---
         */
        mockMvc.perform(put("/api/save-analysis-result")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nalogId\" : 1,\n" +
                        "    \"parametarId\" : 1,\n" +
                        "    \"rezultat\": \"5.3\"\n" +
                        "}")
        ).andExpect(status().isAccepted());
        /*
         *  --- SAVE SECOND LAB RESULT FOR NALOG ---
         */
        mockMvc.perform(put("/api/save-analysis-result")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"nalogId\" : 1,\n" +
                        "    \"parametarId\" : 2,\n" +
                        "    \"rezultat\": \"3.7\"\n" +
                        "}")
        ).andExpect(status().isAccepted());

        mockMvc.perform(put("/api/verify-work-order")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("id", "1"))
                .andExpect(status().isAccepted()); // mora da ima sve lab rezultate da bi bio verifikovan
    }

    @Test
    void createUput() throws Exception {
        mockMvc.perform(post("/api/create-uput")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"tip\" : \"DIJAGNOSTIKA\",\n" +
                                "    \"lbz\" : \"6cfe71bb-e4ee-49dd-a3ad-28e043f8b435\",\n" +
                                "    \"izOdeljenjaId\" : 1,\n" +
                                "    \"zaOdeljenjeId\" : 2,\n" +
                                "    \"lbp\" : \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                                "    \"datumVremeKreiranja\" : 1656658800000\n" + // iz nekog razloga ga kreira tacno u trenutku poziva a ne u specificirano vreme (mozda lose saljem request)
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("uputId").value("2"))
                .andExpect(jsonPath("tip").value("DIJAGNOSTIKA"))
                .andExpect(jsonPath("lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("izOdeljenjaId").value("1"))
                .andExpect(jsonPath("zaOdeljenjeId").value("2"))
                .andExpect(jsonPath("lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("datumVremeKreiranja").exists())
                .andExpect(jsonPath("status").value("NEREALIZOVAN"))
                .andExpect(jsonPath("zahtevaneAnalize").doesNotExist())
                .andExpect(jsonPath("komentar").doesNotExist())
                .andExpect(jsonPath("uputnaDijagnoza").doesNotExist())
                .andExpect(jsonPath("razlogUpucivanja").doesNotExist());
    }

    @Test
    void fetchUput() throws Exception {
        mockMvc.perform(get("/api/fetch-uput")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("uputId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("uputId").value("1"))
                .andExpect(jsonPath("tip").value("LABORATORIJA"))
                .andExpect(jsonPath("lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("izOdeljenjaId").value("1"))
                .andExpect(jsonPath("zaOdeljenjeId").value("2"))
                .andExpect(jsonPath("lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("datumVremeKreiranja").exists())
                .andExpect(jsonPath("status").value("REALIZOVAN"))
                .andExpect(jsonPath("zahtevaneAnalize").value("GLU,HOL"))
                .andExpect(jsonPath("komentar").doesNotExist())
                .andExpect(jsonPath("uputnaDijagnoza").doesNotExist())
                .andExpect(jsonPath("razlogUpucivanja").doesNotExist());
    }

    @Test
    void setUputStatus() throws Exception {
        mockMvc.perform(post("/api/set-uput-status")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"uputId\" : 1,\n" +
                                "    \"statusUputa\" : \"REALIZOVAN\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("uputId").value("1"))
                .andExpect(jsonPath("tip").value("LABORATORIJA"))
                .andExpect(jsonPath("lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("izOdeljenjaId").value("1"))
                .andExpect(jsonPath("zaOdeljenjeId").value("2"))
                .andExpect(jsonPath("lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("datumVremeKreiranja").exists())
                .andExpect(jsonPath("status").value("REALIZOVAN"))
                .andExpect(jsonPath("zahtevaneAnalize").value("GLU,HOL"))
                .andExpect(jsonPath("komentar").doesNotExist())
                .andExpect(jsonPath("uputnaDijagnoza").doesNotExist())
                .andExpect(jsonPath("razlogUpucivanja").doesNotExist());
    }

    @Test
    void uputHistory() throws Exception {
        mockMvc.perform(post("/api/uput-history")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"lbp\" : \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                                "    \"odDatuma\" : \"2022-06-28\",\n" +
                                "    \"doDatuma\" : " + System.currentTimeMillis() + "\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uputId").value("1"))
                .andExpect(jsonPath("$[0].tip").value("LABORATORIJA"))
                .andExpect(jsonPath("$[0].lbz").value("6cfe71bb-e4ee-49dd-a3ad-28e043f8b435"))
                .andExpect(jsonPath("$[0].izOdeljenjaId").value("1"))
                .andExpect(jsonPath("$[0].zaOdeljenjeId").value("2"))
                .andExpect(jsonPath("$[0].lbp").value("237e9877-e79b-12d4-a765-321741963000"))
                .andExpect(jsonPath("$[0].datumVremeKreiranja").exists())
                .andExpect(jsonPath("$[0].status").value("REALIZOVAN"))
                .andExpect(jsonPath("$[0].zahtevaneAnalize").value("GLU,HOL"))
                .andExpect(jsonPath("$[0].komentar").doesNotExist())
                .andExpect(jsonPath("$[0].uputnaDijagnoza").doesNotExist())
                .andExpect(jsonPath("$[0].razlogUpucivanja").doesNotExist());
    }
}
