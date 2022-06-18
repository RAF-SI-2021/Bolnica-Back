import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.bolnica.user.UserApplication;
import raf.si.bolnica.user.configuration.SpringWebConfiguration;

import javax.servlet.ServletContext;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raf.si.bolnica.user.constants.Constants.JWT_KEY;

////@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringWebConfiguration.class})
////@WebAppConfiguration
//@AutoConfigureMockMvc
//@SpringBootTest
@SpringBootTest(classes = UserApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;


//    @Autowired
//    private WebApplicationContext webApplicationContext;

    public String jwt;

//
//    @Test
//    public void givenWac_whenServletContext_thenItProvidesGreetController() {
//        final ServletContext servletContext = webApplicationContext.getServletContext();
//        assertNotNull(servletContext);
//        assertTrue(servletContext instanceof MockServletContext);
//        assertNotNull(webApplicationContext.getBean("userController"));
//    }

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("PORT: " + port);
        ResultActions resultActions = mockMvc.perform(post("http://localhost:" + 8081 + "/api/login")
                        .contentType("application/json")
                        .content("{\n" +
                                "    \"email\": \"test@gmail.com\",\n" +
                                "    \"password\": \"superadmin\"\n" +
                                "}"))
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        jwt = mvcResult.getResponse().getContentAsString().replace("\"", "");
    }

//    private String createJwt() {
//
//        String secretKey = Base64.getEncoder().encodeToString("mysecret".getBytes());
//
//        return Jwts.builder()
//                .setSubject("test@gmail.com")
//                .claim("name", "admin")
//                .claim("surname", "adminic")
//                .claim("title", "titula")
//                .claim("profession", "zanimanje")
//                .claim("LBZ", "e2bf1d7b-4b64-413f-852b-af899ce0a0af")
//                .claim("PBO", 12345)
//                .claim("department", "Hirurgija")
//                .claim("PBB", 1234)
//                .claim("hospital", "Kliničko-bolnički centar \"Dragiša Mišović\"")
//                .claim("roles", "ROLE_ADMIN," + "ROLE_DR_SPEC_ODELJENJA," + "ROLE_DR_SPEC," + "ROLE_DR_SPEC_POV," +
//                        "ROLE_VISA_MED_SESTRA," + "ROLE_MED_SESTRA," + "ROLE_VISI_LABORATORIJSKI_TEHNICAR," +
//                        "ROLE_LABORATORIJSKI_TEHNICAR," + "ROLE_MEDICINSKI_BIOHEMICAR," +
//                        "ROLE_SPECIJALISTA_MEDICINSKE_BIOHEMIJE," + "ROLE_RECEPCIONER")
//                .setIssuer(JWT_KEY)
//                .setExpiration(new Date(System.currentTimeMillis() + 1000000000))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }

    @Test
    void fetchUserByUsername() throws Exception {
        String result = "{\n" +
                "    \"userId\": 1,\n" +
                "    \"name\": \"admin\",\n" +
                "    \"surname\": \"adminic\",\n" +
                "    \"password\": \"$2a$10$9/Mi5luE8LN0UTge9zgclui7Zjkn1RPvRvX7mawpf7O.oCtb2E.5i\",\n" +
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
                "        }\n" +
                "    ]\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/fetch-user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .param("username", "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().json(result));
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void createEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/create-employee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
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
                                "  \"title\": \"Prof. dr. med.\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"string\",\n" +
                        "    \"surname\": \"string\",\n" +
                        "    \"dob\": 1650067200000,\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"jmbg\": \"string\",\n" +
                        "    \"address\": \"string\",\n" +
                        "    \"lbz\": \"91c71fdd-2e95-46f2-96c4-34d47aa90f4a\",\n" +
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
    void removeEmployeeByLbz() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/remove-employee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content("{\n" +
                                "    \"userCredential\": \"superadmin\",\n" +
                                "    \"password\": \"superadmin\"\n" +
                                "}")
                        .param("lbz", "91c71fdd-2e95-46f2-96c4-34d47aa90f4"))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeByLbz() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/get-employee/e2bf1d7b-4b64-413f-852b-af899ce0a0af")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content("{\n" +
                                "    \"userCredential\": \"superadmin\",\n" +
                                "    \"password\": \"superadmin\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"name\": \"admin\",\n" +
                        "    \"surname\": \"adminic\",\n" +
                        "    \"dob\": 1654905600000,\n" +
                        "    \"gender\": \"Muski\",\n" +
                        "    \"jmbg\": \"123456789\",\n" +
                        "    \"address\": \"adresa 1\",\n" +
                        "    \"lbz\": \"e2bf1d7b-4b64-413f-852b-af899ce0a0af\",\n" +
                        "    \"city\": \"SRBIJA\",\n" +
                        "    \"contact\": \"+381 69312321\",\n" +
                        "    \"email\": \"test@gmail.com\",\n" +
                        "    \"title\": \"titula\",\n" +
                        "    \"profession\": \"zanimanje\",\n" +
                        "    \"department\": 1,\n" +
                        "    \"username\": \"superadmin\"\n" +
                        "}"));
    }

    @Test
    void listEmployeesByPbo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/find-employees-pbo/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content("{\n" +
                                "    \"userCredential\": \"superadmin\",\n" +
                                "    \"password\": \"superadmin\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\n" +
                        "    {\n" +
                        "        \"name\": \"admin\",\n" +
                        "        \"surname\": \"adminic\",\n" +
                        "        \"dob\": 1654905600000,\n" +
                        "        \"gender\": \"Muski\",\n" +
                        "        \"jmbg\": \"123456789\",\n" +
                        "        \"address\": \"adresa 1\",\n" +
                        "        \"lbz\": \"e2bf1d7b-4b64-413f-852b-af899ce0a0af\",\n" +
                        "        \"city\": \"SRBIJA\",\n" +
                        "        \"contact\": \"+381 69312321\",\n" +
                        "        \"email\": \"test@gmail.com\",\n" +
                        "        \"title\": \"titula\",\n" +
                        "        \"profession\": \"zanimanje\",\n" +
                        "        \"department\": 1,\n" +
                        "        \"username\": \"superadmin\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"name\": \"string\",\n" +
                        "        \"surname\": \"string\",\n" +
                        "        \"dob\": 1650067200000,\n" +
                        "        \"gender\": \"male\",\n" +
                        "        \"jmbg\": \"string\",\n" +
                        "        \"address\": \"string\",\n" +
                        "        \"lbz\": \"91c71fdd-2e95-46f2-96c4-34d47aa90f4a\",\n" +
                        "        \"city\": \"string\",\n" +
                        "        \"contact\": \"string\",\n" +
                        "        \"email\": \"zaposleni@ibis.rs\",\n" +
                        "        \"title\": \"Prof. dr. med.\",\n" +
                        "        \"profession\": \"Spec. hirurg\",\n" +
                        "        \"department\": 1,\n" +
                        "        \"username\": \"zaposleni\"\n" +
                        "    }\n" +
                        "]"));
    }

    @Test
    void listEmployees() throws Exception {
        mockMvc.perform(post("/api/list-employees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                        .content("{\n" +
                                "    \"userCredential\": \"superadmin\",\n" +
                                "    \"password\": \"superadmin\"\n" +
                                "}")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\n" +
                        "    {\n" +
                        "        \"name\": \"admin\",\n" +
                        "        \"surname\": \"adminic\",\n" +
                        "        \"dob\": 1654905600000,\n" +
                        "        \"gender\": \"Muski\",\n" +
                        "        \"jmbg\": \"123456789\",\n" +
                        "        \"address\": \"adresa 1\",\n" +
                        "        \"lbz\": \"e2bf1d7b-4b64-413f-852b-af899ce0a0af\",\n" +
                        "        \"city\": \"SRBIJA\",\n" +
                        "        \"contact\": \"+381 69312321\",\n" +
                        "        \"email\": \"test@gmail.com\",\n" +
                        "        \"title\": \"titula\",\n" +
                        "        \"profession\": \"zanimanje\",\n" +
                        "        \"department\": 1,\n" +
                        "        \"username\": \"superadmin\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"name\": \"string\",\n" +
                        "        \"surname\": \"string\",\n" +
                        "        \"dob\": 1650067200000,\n" +
                        "        \"gender\": \"male\",\n" +
                        "        \"jmbg\": \"string\",\n" +
                        "        \"address\": \"string\",\n" +
                        "        \"lbz\": \"91c71fdd-2e95-46f2-96c4-34d47aa90f4a\",\n" +
                        "        \"city\": \"string\",\n" +
                        "        \"contact\": \"string\",\n" +
                        "        \"email\": \"zaposleni@ibis.rs\",\n" +
                        "        \"title\": \"Prof. dr. med.\",\n" +
                        "        \"profession\": \"Spec. hirurg\",\n" +
                        "        \"department\": 1,\n" +
                        "        \"username\": \"zaposleni\"\n" +
                        "    }\n" +
                        "]"));
    }

    @Test
    void updateEmployee() {
    }
}
