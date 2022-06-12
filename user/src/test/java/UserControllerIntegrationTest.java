import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import raf.si.bolnica.user.controllers.UserController;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.service.EmailService;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;

import javax.persistence.EntityManager;

import java.util.Base64;
import java.sql.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raf.si.bolnica.user.constants.Constants.JWT_KEY;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class})
@WebAppConfiguration
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    public String jwt = createJwt();

    @MockBean
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserExceptionHandler userExceptionHandler;

    @MockBean
    private OdeljenjeService odeljenjeService;

    @MockBean
    private LoggedInUser loggedInUser;

    @MockBean
    private EmailService emailService;

    @MockBean
    private EntityManager entityManager;


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createJwt() {

        String secretKey = Base64.getEncoder().encodeToString("mysecret".getBytes());

        return Jwts.builder()
                .setSubject("test@gmail.com")
                .claim("name", "admin")
                .claim("surname", "adminic")
                .claim("title", "titula")
                .claim("profession", "zanimanje")
                .claim("LBZ", "e2bf1d7b-4b64-413f-852b-af899ce0a0af")
                .claim("PBO", 12345)
                .claim("department", "Hirurgija")
                .claim("PBB", 1234)
                .claim("hospital", "Kliničko-bolnički centar \"Dragiša Mišović\"")
                .claim("roles", "ROLE_ADMIN," + "ROLE_DR_SPEC_ODELJENJA," + "ROLE_DR_SPEC," + "ROLE_DR_SPEC_POV," +
                        "ROLE_VISA_MED_SESTRA," + "ROLE_MED_SESTRA," + "ROLE_VISI_LABORATORIJSKI_TEHNICAR," +
                        "ROLE_LABORATORIJSKI_TEHNICAR," + "ROLE_MEDICINSKI_BIOHEMICAR," +
                        "ROLE_SPECIJALISTA_MEDICINSKE_BIOHEMIJE," + "ROLE_RECEPCIONER")
                .setIssuer(JWT_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 1000000000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

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
        mockMvc.perform(get("/api/fetch-user")
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
        mockMvc.perform(post("/api/create-employee")
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
        mockMvc.perform(delete("/api/remove-employee")
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
        mockMvc.perform(get("/api/get-employee/e2bf1d7b-4b64-413f-852b-af899ce0a0af")
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
        mockMvc.perform(get("/api/find-employees-pbo/1")
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
