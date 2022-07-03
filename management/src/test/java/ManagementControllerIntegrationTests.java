import com.google.gson.*;
import org.apache.http.HttpHeaders;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import raf.si.bolnica.management.ManagementApplication;
import raf.si.bolnica.management.configuration.SpringWebConfiguration;
import raf.si.bolnica.management.jwt.JwtProperties;
import org.springframework.http.*;
import javax.servlet.ServletContext;
import java.lang.reflect.Type;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringWebConfiguration.class, JwtProperties.class})
@SpringBootTest(classes = ManagementApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagementControllerIntegrationTests {

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

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        final ServletContext servletContext = webApplicationContext.getServletContext();
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("managementController"));
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
        System.out.printf(jwt);
    }

    @Test
    void addAllergenToPatient() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/add-allergen")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "  \"naziv\": \"Penicilin\"\n" +
                        "}"));


        resultActions.andDo(print()).andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("{\n" +
                        "    \"id\": 5,\n" +
                        "    \"alergen\": \"Penicilin\",\n" +
                        "    \"zdravstveniKartonId\": 1\n" +
                        "}"));
    }


    @Test
    void addVaccineToPatient() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/add-vaccine")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "  \"naziv\": \"SYNFLORIX\",\n" +
                        "  \"datumVakcinacije\": 1650067200000\n" +
                        "}"));


        resultActions.andDo(print()).andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("{\n" +
                        "    \"id\": 3,\n" +
                        "    \"zdravstveniKartonId\": 1\n" +
                        "}"));
    }

    @Test
    void registerLekarskiIzvestaj() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/add-lekarski-izvestaj")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "  \"objektivniNalaz\": \"String\",\n" +
                        "  \"dijagnoza\": \"String\",\n" +
                        "  \"predlozenaTerapija\": \"String\",\n" +
                        "  \"savet\": \"String\",\n" +
                        "  \"indikatorPoverljivosti\": false\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("{\n" +
                        "    \"stanjePacijentaId\": 1,\n" +
                        "    \"lbpPacijenta\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "    \"lbzLekaraSpecijaliste\":\"6cfe71bb-e4ee-49dd-a3ad-28e043f8b435\",\n"+
                        "    \"indikatorPoverljivosti\":false,\n"+
                        "    \"objektivniNalaz\": \"String\",\n" +
                        "    \"dijagnoza\": \"String\",\n" +
                        "    \"predlozenaTerapija\": \"String\",\n" +
                        "    \"savet\": \"String\",\n" +
                        "    \"obrisan\":false\n"+
                        "}"));
    }

    @Test
    void searchLekarskiIzvestaji() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/search-lekarski-izvestaj")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "  \"date\": 1650067200000,\n" +
                        "  \"end\": 1670067200000\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk()).andDo(print());
    }

    @Test
    void registerOtpusnaLista() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/add-otpusna-lista")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"pbo\": 1,\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "  \"prateceDijagnoze\": \"String\",\n" +
                        "  \"anamneza\": \"String\",\n" +
                        "  \"analize\": \"String\",\n" +
                        "  \"tokBolesti\": \"String\",\n" +
                        "  \"zakljucak\": \"String\",\n" +
                        "  \"terapija\": \"String\"\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("{\n" +
                        "    \"otpusnaListaId\": 1,\n" +
                        "    \"lbpPacijenta\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "    \"hospitalizacijaId\":1,\n"+
                        "    \"lbzOrdinirajucegLekara\": \"6cfe71bb-e4ee-49dd-a3ad-28e043f8b435\",\n" +
                        "    \"lbzNacelnikOdeljenja\": \"6cfe71bb-e4ee-49dd-a3ad-28e043f8b435\",\n"+
                        "    \"prateceDijagnoze\": \"String\", \n" +
                        "    \"anamneza\": \"String\", \n" +
                        "    \"analize\": \"String\", \n" +
                        "    \"tokBolesti\": \"String\", \n"+
                        "    \"zakljucak\": \"String\", \n" +
                        "    \"terapija\": \"String\" \n"+
                        "}"));
    }


    @Test
    void searchOtpusneListe() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/search-otpusna-lista")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\",\n" +
                        "  \"start\": 1650067200000,\n" +
                        "  \"end\": 1670067200000\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk()).andDo(print());
    }


    @AfterAll
    public void cleanup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    }


}
