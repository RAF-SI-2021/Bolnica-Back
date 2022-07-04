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
import raf.si.bolnica.management.response.PacijentResponseDTO;

import javax.servlet.ServletContext;
import java.lang.reflect.Type;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public String lbz;
    public String lbp;
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

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/create-patient")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"jmbg\": \"String1\",\n" +
                        "  \"ime\": \"String1\",\n" +
                        "  \"imeRoditelja\": \"String1\",\n" +
                        "  \"prezime\": \"String1\",\n" +
                        "  \"pol\": \"MUSKI\",\n" +
                        "  \"datumRodjenja\": 1650067200000,\n" +
                        "  \"datumVremeSmrti\": 1660067200000,\n" +
                        "  \"mestoRodjenja\": \"String1\",\n" +
                        "  \"zemljaDrzavljanstva\": \"AFG\",\n" +
                        "  \"adresa\": \"String1\",\n" +
                        "  \"mestoStanovanja\": \"String1\",\n" +
                        "  \"zemljaStanovanja\": \"AFG\",\n" +
                        "  \"kontaktTelefon\": \"String1\",\n" +
                        "  \"email\": \"String1\",\n" +
                        "  \"jmbgStaratelj\": \"String1\",\n" +
                        "  \"imeStaratelj\": \"String1\",\n" +
                        "  \"porodicniStatus\": \"RAZVEDENI\",\n" +
                        "  \"bracniStatus\": \"UDOVAC\",\n" +
                        "  \"brojDece\": 1,\n" +
                        "  \"stepenStrucneSpreme\": \"VISOKO\",\n" +
                        "  \"zanimanje\": \"String1\"\n" +
                        "}"));




        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"jmbg\": \"String1\",\n" +
                        "  \"ime\": \"String1\",\n" +
                        "  \"imeRoditelja\": \"String1\",\n" +
                        "  \"prezime\": \"String1\",\n" +
                        "  \"pol\": \"MUSKI\",\n" +
                        "  \"datumRodjenja\": 1650067200000,\n" +
                        "  \"datumVremeSmrti\": 1660067200000,\n" +
                        "  \"mestoRodjenja\": \"String1\",\n" +
                        "  \"zemljaDrzavljanstva\": \"AFG\",\n" +
                        "  \"adresa\": \"String1\",\n" +
                        "  \"mestoStanovanja\": \"String1\",\n" +
                        "  \"zemljaStanovanja\": \"AFG\",\n" +
                        "  \"kontaktTelefon\": \"String1\",\n" +
                        "  \"email\": \"String1\",\n" +
                        "  \"jmbgStaratelj\": \"String1\",\n" +
                        "  \"imeStaratelj\": \"String1\",\n" +
                        "  \"porodicniStatus\": \"RAZVEDENI\",\n" +
                        "  \"bracniStatus\": \"UDOVAC\",\n" +
                        "  \"brojDece\": 1,\n" +
                        "  \"stepenStrucneSpreme\": \"VISOKO\",\n" +
                        "  \"zanimanje\": \"String1\"\n" +
                        "}"));

        String content = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(content);
        content = content.replace("\"","");
        content = content.replace(":",",");
        String[] tem2 = content.split(",");
        for(int i = 0; i<tem2.length; i++){
            if (tem2[i].equals("lbp")){
                System.out.println(tem2[i]);
                System.out.println(tem2[i+1]);
                lbp = tem2[i+1];
                lbp = lbp.replace("}","");
                System.out.println(lbp);
                break;
            }
        }

        RestTemplate restTemplate1 = new RestTemplate();
        org.springframework.http.HttpHeaders headers1 = new org.springframework.http.HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        headers1.setBearerAuth(jwt);
        HttpEntity<String> entity1 = new HttpEntity<>( headers1);
        ResponseEntity<String> response1 = restTemplate1.exchange("http://localhost:8081/api/find-employees-pbo/1", HttpMethod.GET, entity1, String.class);
        String tem1 = response1.getBody();
        System.out.printf(tem1);
        tem1 = tem1.replace("\"","");
        tem1 = tem1.replace(":",",");
        String[] tem = tem1.split(",");
        for (String s:tem
             ) {
            System.out.println(s);
        }
        for(int i = 0; i<tem.length; i++){
            if (tem[i].equals("lbz")){
                System.out.println(tem[i]);
                System.out.println(tem[i+1]);
                lbz = tem[i+1];
                break;
            }
        }

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


        resultActions.andDo(print()).andExpect(status().isOk())
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


        resultActions.andDo(print()).andExpect(status().isOk())
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

        resultActions.andDo(print()).andExpect(status().isOk())
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

        resultActions.andDo(print()).andExpect(status().isOk());
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

        resultActions.andDo(print()).andExpect(status().isOk());
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

        resultActions.andDo(print()).andExpect(status().isOk())
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
    void createPatient() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/create-patient")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"jmbg\": \"String\",\n" +
                        "  \"ime\": \"String\",\n" +
                        "  \"imeRoditelja\": \"String\",\n" +
                        "  \"prezime\": \"String\",\n" +
                        "  \"pol\": \"MUSKI\",\n" +
                        "  \"datumRodjenja\": 1650067200000,\n" +
                        "  \"datumVremeSmrti\": 1660067200000,\n" +
                        "  \"mestoRodjenja\": \"String\",\n" +
                        "  \"zemljaDrzavljanstva\": \"AFG\",\n" +
                        "  \"adresa\": \"String\",\n" +
                        "  \"mestoStanovanja\": \"String\",\n" +
                        "  \"zemljaStanovanja\": \"AFG\",\n" +
                        "  \"kontaktTelefon\": \"String\",\n" +
                        "  \"email\": \"String\",\n" +
                        "  \"jmbgStaratelj\": \"String\",\n" +
                        "  \"imeStaratelj\": \"String\",\n" +
                        "  \"porodicniStatus\": \"RAZVEDENI\",\n" +
                        "  \"bracniStatus\": \"UDOVAC\",\n" +
                        "  \"brojDece\": 1,\n" +
                        "  \"stepenStrucneSpreme\": \"VISOKO\",\n" +
                        "  \"zanimanje\": \"String\"\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"jmbg\": \"String\",\n" +
                        "  \"ime\": \"String\",\n" +
                        "  \"imeRoditelja\": \"String\",\n" +
                        "  \"prezime\": \"String\",\n" +
                        "  \"pol\": \"MUSKI\",\n" +
                        "  \"datumRodjenja\": 1650067200000,\n" +
                        "  \"datumVremeSmrti\": 1660067200000,\n" +
                        "  \"mestoRodjenja\": \"String\",\n" +
                        "  \"zemljaDrzavljanstva\": \"AFG\",\n" +
                        "  \"adresa\": \"String\",\n" +
                        "  \"mestoStanovanja\": \"String\",\n" +
                        "  \"zemljaStanovanja\": \"AFG\",\n" +
                        "  \"kontaktTelefon\": \"String\",\n" +
                        "  \"email\": \"String\",\n" +
                        "  \"jmbgStaratelj\": \"String\",\n" +
                        "  \"imeStaratelj\": \"String\",\n" +
                        "  \"porodicniStatus\": \"RAZVEDENI\",\n" +
                        "  \"bracniStatus\": \"UDOVAC\",\n" +
                        "  \"brojDece\": 1,\n" +
                        "  \"stepenStrucneSpreme\": \"VISOKO\",\n" +
                        "  \"zanimanje\": \"String\"\n" +
                        "}"));
    }


    @Test
    void updatePatient() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/update-patient/237e9877-e79b-12d4-a765-321741963000")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"jmbg\": \"String2\",\n" +
                        "  \"ime\": \"String2\",\n" +
                        "  \"imeRoditelja\": \"String2\",\n" +
                        "  \"prezime\": \"String2\",\n" +
                        "  \"pol\": \"MUSKI\",\n" +
                        "  \"datumRodjenja\": 1650065200000,\n" +
                        "  \"datumVremeSmrti\": 1660067200000,\n" +
                        "  \"mestoRodjenja\": \"String2\",\n" +
                        "  \"zemljaDrzavljanstva\": \"AFG\",\n" +
                        "  \"adresa\": \"String2\",\n" +
                        "  \"mestoStanovanja\": \"String2\",\n" +
                        "  \"zemljaStanovanja\": \"AFG\",\n" +
                        "  \"kontaktTelefon\": \"String2\",\n" +
                        "  \"email\": \"String2\",\n" +
                        "  \"jmbgStaratelj\": \"String2\",\n" +
                        "  \"imeStaratelj\": \"String2\",\n" +
                        "  \"porodicniStatus\": \"RAZVEDENI\",\n" +
                        "  \"bracniStatus\": \"UDOVAC\",\n" +
                        "  \"brojDece\": 1,\n" +
                        "  \"stepenStrucneSpreme\": \"VISOKO\",\n" +
                        "  \"zanimanje\": \"String2\"\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"jmbg\": \"String2\",\n" +
                        "  \"ime\": \"String2\",\n" +
                        "  \"imeRoditelja\": \"String2\",\n" +
                        "  \"prezime\": \"String2\",\n" +
                        "  \"pol\": \"MUSKI\",\n" +
                        "  \"datumRodjenja\": 1650065200000,\n" +
                        "  \"datumVremeSmrti\": 1660067200000,\n" +
                        "  \"mestoRodjenja\": \"String2\",\n" +
                        "  \"zemljaDrzavljanstva\": \"AFG\",\n" +
                        "  \"adresa\": \"String2\",\n" +
                        "  \"mestoStanovanja\": \"String2\",\n" +
                        "  \"zemljaStanovanja\": \"AFG\",\n" +
                        "  \"kontaktTelefon\": \"String2\",\n" +
                        "  \"email\": \"String2\",\n" +
                        "  \"jmbgStaratelj\": \"String2\",\n" +
                        "  \"imeStaratelj\": \"String2\",\n" +
                        "  \"porodicniStatus\": \"RAZVEDENI\",\n" +
                        "  \"bracniStatus\": \"UDOVAC\",\n" +
                        "  \"brojDece\": 1,\n" +
                        "  \"stepenStrucneSpreme\": \"VISOKO\",\n" +
                        "  \"zanimanje\": \"String2\"\n" +
                        "}"));
    }

    @Test
    void removePatient() throws Exception{

        mockMvc.perform(delete("/api/remove-patient/"+lbp)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void fetchPatientLbp() throws Exception{

        mockMvc.perform(get("/api/fetch-patient/237e9877-e79b-12d4-a765-321741963000")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void fetchZdravstveniKartonLbp() throws Exception{

        mockMvc.perform(get("/api/fetch-zdravstveni-karton/237e9877-e79b-12d4-a765-321741963000")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void fetchPatientDataLbp() throws Exception{

        mockMvc.perform(get("/api/fetch-patient-data/237e9877-e79b-12d4-a765-321741963000")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)).andDo(print())
                .andExpect(status().isOk());


    }
    @Test
    void setAppointment() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/set-appointment")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"note\": \"String\",\n" +
                        "  \"dateAndTimeOfAppointment\": 1650067200000,\n" +
                        "  \"lbz\": \""+lbz+"\",\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\"\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"zakazaniPregledId\": 1,\n" +
                        "  \"datumIVremePregleda\": 1650067200000,\n" +
                        "  \"statusPregleda\": \"ZAKAZANO\",\n" +
                        "  \"prispecePacijenta\": \"NIJE_DOSAO\",\n" +
                        "  \"lbzLekara\": \""+lbz+"\",\n" +
                        "  \"lbzSestre\": \""+lbz+"\"\n" +
                        "}"));
    }

    @Test
    void fetchPreglediLbp() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/fetch-examinations/237e9877-e79b-12d4-a765-321741963000")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .param("page", String.valueOf(5))
                .param("size", String.valueOf(5))
                .contentType("application/json")
                .content("{\n" +
                        "  \"from\": 1640067200000,\n" +
                        "  \"to\": 1650067200000,\n" +
                        "  \"on\": 1650067000000\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    void fetchIstorijaBolestiLbp() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/fetch-istorija-bolesti/237e9877-e79b-12d4-a765-321741963000")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .param("page", String.valueOf(5))
                .param("size", String.valueOf(5))
                .contentType("application/json")
                .content("{\n" +
                        "  \"dijagnoza\": \"String\"\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    void filterPatients() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/filter-patients")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"jmbg\": \"String\",\n" +
                        "  \"ime\": \"String\",\n" +
                        "  \"prezime\": \"String\",\n" +
                        "  \"lbp\": \"237e9877-e79b-12d4-a765-321741963000\"\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk());
    }

    @Test
    void listAppointmentsByLBZ() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/list-appointments-by-lbz")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"lbz\": \""+lbz+"\",\n" +
                        "  \"date\": \"1650067200000\"\n" +
                        "}"));

        resultActions.andDo(print()).andExpect(status().isOk());
    }
    @AfterAll
    public void cleanup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    }


}
