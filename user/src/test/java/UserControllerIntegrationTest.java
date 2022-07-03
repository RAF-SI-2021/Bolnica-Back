import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.apache.http.HttpHeaders;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.bolnica.user.UserApplication;
import raf.si.bolnica.user.configuration.SpringWebConfiguration;
import raf.si.bolnica.user.jwt.JwtProperties;
import raf.si.bolnica.user.properties.EmailProperties;
import raf.si.bolnica.user.responses.UserDataResponseDTO;
import raf.si.bolnica.user.responses.UserResponseDTO;

import javax.servlet.ServletContext;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//docker-compose up docker-mysql

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringWebConfiguration.class, EmailProperties.class, JwtProperties.class})
@SpringBootTest(classes = UserApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public String jwt;

    public String lbz;
    public String lbz1;
    public String lbz2;

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
        assertNotNull(webApplicationContext.getBean("userController"));
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

        ResultActions resultActions = mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content("{\n" +
                                "    \"email\": \"test@gmail.com\",\n" +
                                "    \"password\": \"superadmin\"\n" +
                                "}"))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        jwt = mvcResult.getResponse().getContentAsString().replace("\"", "");

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/create-employee")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"address\": \"string\",\n" +
                        "  \"city\": \"string\",\n" +
                        "  \"contact\": \"string\",\n" +
                        "  \"department\": 1,\n" +
                        "  \"dob\": \"2022-04-16\",\n" +
                        "  \"email\": \"zaposleni@ibis.rs\",\n" +
                        "  \"gender\": \"male\",\n" +
                        "  \"jmbg\": \"string\",\n" +
                        "  \"name\": \"string\",\n" +
                        "  \"profession\": \"Spec. hirurg\",\n" +
                        "  \"surname\": \"string\",\n" +
                        "  \"title\": \"Prof. dr. med.\",\n" +
                        "  \"roles\": [ \"ROLE_DR_SPEC\" ]\n" +
                        "}"));
        String content = resultActions.andReturn().getResponse().getContentAsString();



        UserDataResponseDTO responseDTO = g.fromJson(content, UserDataResponseDTO.class);

        lbz = responseDTO.getLbz().toString();


        resultActions.andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"string\",\n" +
                        "    \"surname\": \"string\",\n" +
                        "    \"dob\": 1650067200000,\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"jmbg\": \"string\",\n" +
                        "    \"address\": \"string\",\n" +
                        "    \"lbz\": \"" + responseDTO.getLbz() + "\",\n" +
                        "    \"city\": \"string\",\n" +
                        "    \"contact\": \"string\",\n" +
                        "    \"email\": \"zaposleni@ibis.rs\",\n" +
                        "    \"title\": \"Prof. dr. med.\",\n" +
                        "    \"profession\": \"Spec. hirurg\",\n" +
                        "    \"department\": 1,\n" +
                        "    \"username\": \"zaposleni\",\n" +
                        "    \"obrisan\" : false\n" +
                        "}"));

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/create-employee")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"address\": \"string1\",\n" +
                        "  \"city\": \"string1\",\n" +
                        "  \"contact\": \"string1\",\n" +
                        "  \"department\": 2,\n" +
                        "  \"dob\": \"2022-04-16\",\n" +
                        "  \"email\": \"zaposleni1@ibis.rs\",\n" +
                        "  \"gender\": \"male\",\n" +
                        "  \"jmbg\": \"string1\",\n" +
                        "  \"name\": \"string1\",\n" +
                        "  \"profession\": \"Spec. hirurg\",\n" +
                        "  \"surname\": \"string1\",\n" +
                        "  \"title\": \"Prof. dr. med.\",\n" +
                        "  \"roles\": [ \"ROLE_DR_SPEC\" ]\n" +
                        "}"));

        content = resultActions.andReturn().getResponse().getContentAsString();

        responseDTO = g.fromJson(content, UserDataResponseDTO.class);

        lbz1 = responseDTO.getLbz().toString();


        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"string1\",\n" +
                        "    \"surname\": \"string1\",\n" +
                        "    \"dob\": 1650067200000,\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"jmbg\": \"string1\",\n" +
                        "    \"address\": \"string1\",\n" +
                        "    \"lbz\": \"" + responseDTO.getLbz() + "\",\n" +
                        "    \"city\": \"string1\",\n" +
                        "    \"contact\": \"string1\",\n" +
                        "    \"email\": \"zaposleni1@ibis.rs\",\n" +
                        "    \"title\": \"Prof. dr. med.\",\n" +
                        "    \"profession\": \"Spec. hirurg\",\n" +
                        "    \"department\": 2,\n" +
                        "    \"username\": \"zaposleni1\",\n" +
                        "    \"obrisan\" : false\n" +
                        "}"));
    }

    @AfterAll
    public void cleanup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        mockMvc.perform(delete("/api/remove-employee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("lbz", lbz))
                .andExpect(status().isOk());
    }

    @Test
    void loginUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/login")
                        .contentType("application/json")
                        .content("{\n" +
                                "    \"email\": \"test@gmail.com\",\n" +
                                "    \"password\": \"superadmin\"\n" +
                                "}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void fetchUserByUsername() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/fetch-user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("username", "test@gmail.com"));
        String content = resultActions.andReturn().getResponse().getContentAsString();
        UserResponseDTO responseDTO = g.fromJson(content, UserResponseDTO.class);


        String result = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"name\": \"admin\",\n" +
                "    \"surname\": \"adminic\",\n" +
                "    \"password\": \"" + responseDTO.getPassword() + "\",\n" +
                "    \"email\": \"test@gmail.com\",\n" +
                "    \"roles\": [\n" +
                "        {\n" +
                "            \"roleId\": 8,\n" +
                "            \"name\": \"ROLE_LABORATORIJSKI_TEHNICAR\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 5,\n" +
                "            \"name\": \"ROLE_VISA_MED_SESTRA\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 9,\n" +
                "            \"name\": \"ROLE_MEDICINSKI_BIOHEMICAR\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 3,\n" +
                "            \"name\": \"ROLE_DR_SPEC\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 7,\n" +
                "            \"name\": \"ROLE_VISI_LABORATORIJSKI_TEHNICAR\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 1,\n" +
                "            \"name\": \"ROLE_ADMIN\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 6,\n" +
                "            \"name\": \"ROLE_MED_SESTRA\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 2,\n" +
                "            \"name\": \"ROLE_DR_SPEC_ODELJENJA\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 4,\n" +
                "            \"name\": \"ROLE_DR_SPEC_POV\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"roleId\": 10,\n" +
                "            \"name\": \"ROLE_SPECIJALISTA_MEDICINSKE_BIOHEMIJE\"\n" +
                "        },\n" +
                "       {\n" +
                "            \"roleId\": 11,\n" +
                "            \"name\": \"ROLE_RECEPCIONER\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

                resultActions.andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void getEmployeeByLbz() throws Exception {

        mockMvc.perform(get("/api/get-employee/" + lbz)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"string\",\n" +
                        "    \"surname\": \"string\",\n" +
                        "    \"dob\": 1650067200000,\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"jmbg\": \"string\",\n" +
                        "    \"address\": \"string\",\n" +
                        "    \"lbz\": \"" + lbz + "\",\n" +
                        "    \"city\": \"string\",\n" +
                        "    \"contact\": \"string\",\n" +
                        "    \"email\": \"zaposleni@ibis.rs\",\n" +
                        "    \"title\": \"Prof. dr. med.\",\n" +
                        "    \"profession\": \"Spec. hirurg\",\n" +
                        "    \"department\": 1,\n" +
                        "    \"username\": \"zaposleni\"\n" +
                        "}"));
    }

    @Test
    void listEmployeesByPbo() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/find-employees-pbo/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
        String content = resultActions.andReturn().getResponse().getContentAsString();

        Type listType = new TypeToken<ArrayList<UserDataResponseDTO>>(){}.getType();

        List<UserDataResponseDTO> listResponseDTO = g.fromJson(content, listType);

        resultActions.andExpect(status().isOk())
                .andExpect(content().json("[\n" +
                        "    {\n" +
                        "        \"name\": \"admin\",\n" +
                        "        \"surname\": \"adminic\",\n" +
                        "        \"dob\": " + listResponseDTO.get(0).getDob().getTime() + ",\n" +
                        "        \"gender\": \"Muski\",\n" +
                        "        \"jmbg\": \"123456789\",\n" +
                        "        \"address\": \"adresa 1\",\n" +
                        "        \"lbz\": \"" + listResponseDTO.get(0).getLbz() + "\",\n" +
                        "        \"city\": \"SRBIJA\",\n" +
                        "        \"contact\": \"+381 69312321\",\n" +
                        "        \"email\": \"test@gmail.com\",\n" +
                        "        \"title\": \"Dr. sci. med\",\n" +
                        "        \"profession\": \"Spec. endrokrinolog\",\n" +
                        "        \"department\": 1,\n" +
                        "        \"username\": \"superadmin\"\n" +
                        "    },\n" +
                                "{\n" +
                                "    \"name\": \"string\",\n" +
                                "    \"surname\": \"string\",\n" +
                                "    \"dob\": 1650067200000,\n" +
                                "    \"gender\": \"male\",\n" +
                                "    \"jmbg\": \"string\",\n" +
                                "    \"address\": \"string\",\n" +
                                "    \"lbz\": \"" + lbz + "\",\n" +
                                "    \"city\": \"string\",\n" +
                                "    \"contact\": \"string\",\n" +
                                "    \"email\": \"zaposleni@ibis.rs\",\n" +
                                "    \"title\": \"Prof. dr. med.\",\n" +
                                "    \"profession\": \"Spec. hirurg\",\n" +
                                "    \"department\": 1,\n" +
                                "    \"username\": \"zaposleni\"\n" +
                                "}\n" +
                        "]"));
    }

    @Test
    void listEmployees() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/list-employees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .contentType("application/json")
                        .content("{}")
                        .param("page", "1")
                        .param("size", "5"));

        String content = resultActions.andReturn().getResponse().getContentAsString();

        Type listType = new TypeToken<ArrayList<UserDataResponseDTO>>(){}.getType();

        List<UserDataResponseDTO> listResponseDTO = g.fromJson(content, listType);

        resultActions.andExpect(status().isOk())
                .andExpect(content().json("[\n" +
                        "    {\n" +
                        "        \"name\": \"admin\",\n" +
                        "        \"surname\": \"adminic\",\n" +
                        "        \"dob\": " + listResponseDTO.get(0).getDob().getTime() + ",\n" +
                        "        \"gender\": \"Muski\",\n" +
                        "        \"jmbg\": \"123456789\",\n" +
                        "        \"address\": \"adresa 1\",\n" +
                        "        \"lbz\": \"" + listResponseDTO.get(0).getLbz() + "\",\n" +
                        "        \"city\": \"SRBIJA\",\n" +
                        "        \"contact\": \"+381 69312321\",\n" +
                        "        \"email\": \"test@gmail.com\",\n" +
                        "        \"title\": \"Dr. sci. med\",\n" +
                        "        \"profession\": \"Spec. endrokrinolog\",\n" +
                        "        \"department\": 1,\n" +
                        "        \"username\": \"superadmin\"\n" +
                        "    },\n" +
                        "{\n" +
                        "    \"name\": \"string\",\n" +
                        "    \"surname\": \"string\",\n" +
                        "    \"dob\": 1650067200000,\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"jmbg\": \"string\",\n" +
                        "    \"address\": \"string\",\n" +
                        "    \"lbz\": \"" + lbz + "\",\n" +
                        "    \"city\": \"string\",\n" +
                        "    \"contact\": \"string\",\n" +
                        "    \"email\": \"zaposleni@ibis.rs\",\n" +
                        "    \"title\": \"Prof. dr. med.\",\n" +
                        "    \"profession\": \"Spec. hirurg\",\n" +
                        "    \"department\": 1,\n" +
                        "    \"username\": \"zaposleni\"\n" +
                        "},\n" +
                        "{\n" +
                        "    \"name\": \"string2\",\n" +
                        "    \"surname\": \"string2\",\n" +
                        "    \"dob\": 1650067200000,\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"jmbg\": \"string2\",\n" +
                        "    \"address\": \"string2\",\n" +
                        "    \"lbz\": \"" + lbz2 + "\",\n" +
                        "    \"city\": \"string2\",\n" +
                        "    \"contact\": \"string2\",\n" +
                        "    \"email\": \"zaposleni2@ibis.rs\",\n" +
                        "    \"title\": \"Prof. dr. med.\",\n" +
                        "    \"profession\": \"Spec. hirurg\",\n" +
                        "    \"department\": 3,\n" +
                        "    \"username\": \"zaposleni2\"\n" +
                        "}\n" +
                        "]"));
    }

    @Test
    void deleteUserByLBZ() throws Exception{

        mockMvc.perform(delete("/api/remove-employee/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("lbz", lbz1)).andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void addUser() throws Exception{
        ResultActions resultActions = mockMvc.perform(post("/api/create-employee")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"address\": \"string2\",\n" +
                        "  \"city\": \"string2\",\n" +
                        "  \"contact\": \"string2\",\n" +
                        "  \"department\": 3,\n" +
                        "  \"dob\": \"2022-04-16\",\n" +
                        "  \"email\": \"zaposleni2@ibis.rs\",\n" +
                        "  \"gender\": \"male\",\n" +
                        "  \"jmbg\": \"string2\",\n" +
                        "  \"name\": \"string2\",\n" +
                        "  \"profession\": \"Spec. hirurg\",\n" +
                        "  \"surname\": \"string2\",\n" +
                        "  \"title\": \"Prof. dr. med.\",\n" +
                        "  \"roles\": [ \"ROLE_MED_SESTRA\" ]\n" +
                        "}"));
        String content = resultActions.andReturn().getResponse().getContentAsString();

        UserDataResponseDTO responseDTO = g.fromJson(content, UserDataResponseDTO.class);
        lbz2 = String.valueOf(responseDTO.getLbz());
        resultActions.andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"string2\",\n" +
                        "    \"surname\": \"string2\",\n" +
                        "    \"dob\": 1650067200000,\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"jmbg\": \"string2\",\n" +
                        "    \"address\": \"string2\",\n" +
                        "    \"lbz\": \"" + responseDTO.getLbz() + "\",\n" +
                        "    \"city\": \"string2\",\n" +
                        "    \"contact\": \"string2\",\n" +
                        "    \"email\": \"zaposleni2@ibis.rs\",\n" +
                        "    \"title\": \"Prof. dr. med.\",\n" +
                        "    \"profession\": \"Spec. hirurg\",\n" +
                        "    \"department\": 3,\n" +
                        "    \"username\": \"zaposleni2\",\n" +
                        "    \"obrisan\" : false\n" +
                        "}"));
    }

    @Test
    void getEmployeeInfo() throws Exception {

        mockMvc.perform(get("/api/employee-info/" + lbz)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"string\",\n" +
                        "    \"surname\": \"string\",\n" +
                        "    \"profession\": \"Spec. hirurg\"\n" +
                        "}"));
    }

    @Test
    void findDrSpecOdeljenjaByPbo() throws Exception {

        mockMvc.perform(get("/api/find-dr-spec-odeljenja/" + "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void updateUser() throws Exception{
        ResultActions resultActions = mockMvc.perform(put("/api/update-employee")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType("application/json")
                .content("{\n" +
                        "  \"address\": \"string\",\n" +
                        "  \"city\": \"string\",\n" +
                        "  \"contact\": \"string\",\n" +
                        "  \"department\": 1,\n" +
                        "  \"dob\": \"2022-04-16\",\n" +
                        "  \"email\": \"zaposleni@ibis.rs\",\n" +
                        "  \"gender\": \"male\",\n" +
                        "  \"jmbg\": \"string\",\n" +
                        "  \"name\": \"string\",\n" +
                        "  \"profession\": \"Spec. hirurg\",\n" +
                        "  \"surname\": \"string\",\n" +
                        "  \"title\": \"Prof. dr. med.\",\n" +
                        "  \"lbz\": \"" + lbz +"\", \n" +
                        " \"oldPassword\": \"\",\n" +
                        " \"newPassword\": \"string\"\n" +
                        "}"));
        String content = resultActions.andReturn().getResponse().getContentAsString();

        UserDataResponseDTO responseDTO = g.fromJson(content, UserDataResponseDTO.class);

        resultActions.andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"string\",\n" +
                        "    \"surname\": \"string\",\n" +
                        "    \"datumRodjenja\": 1650067200000,\n" +
                        "    \"pol\": \"male\",\n" +
                        "    \"jmbg\": \"string\",\n" +
                        "    \"adresaStanovanja\": \"string\",\n" +
                        "    \"mestoStanovanja\": \"string\",\n" +
                        "    \"lbz\": \"" + lbz + "\",\n" +
                        "    \"kontaktTelefon\": \"string\",\n" +
                        "    \"email\": \"zaposleni@ibis.rs\",\n" +
                        "    \"titula\": \"Prof. dr. med.\",\n" +
                        "    \"zanimanje\": \"Spec. hirurg\",\n" +
                        "    \"korisnickoIme\": \"zaposleni\",\n" +
                        "    \"obrisan\" : false\n" +
                        "}"));
    }

    @Test
    void getAllDepartments() throws Exception{
        mockMvc.perform(get("/api/departments/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk()).andDo(print());
    }



    @Test
    void searchForDepartmentByName() throws Exception{
        mockMvc.perform(get("/api/department/search")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .param("name","Neurohirurgija"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("[{\n" +
                        "    \"odeljenjeId\": 8,\n" +
                        "    \"poslovniBrojOdeljenja\": 55555,\n" +
                        "    \"naziv\": \"Neurohirurgija\",\n" +
                        "    \"obrisan\" : false\n" +
                        "}]"));
    }


    @Test
    void getAllHospitals() throws Exception{
        mockMvc.perform(get("/api/hospitals/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().json("[{\n" +
                        "    \"zdravstvenaUstanovaId\": 1,\n" +
                        "    \"poslovniBrojBolnice\": 1234,\n" +
                        "    \"naziv\": \"Kliničko-bolnički centar \\\"Dragiša Mišović\\\"\",\n"+
                        "    \"skracenNaziv\": \"KBC Dragiša Mišović\",\n" +
                        "    \"mesto\": \"Beograd\",\n" +
                        "    \"adresa\": \"Heroja Milana Tepića 1, Beograd\",\n" +
                        "    \"delatnost\": \"Ginekologija i akušerstvo\",\n" +
                        "    \"obrisan\" : false\n" +
                        "}]"));
    }
}
