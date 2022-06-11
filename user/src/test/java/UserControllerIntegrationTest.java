import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.controllers.LoginController;
import raf.si.bolnica.user.controllers.UserController;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.jwt.JwtProperties;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.requests.LoginRequestDTO;
import raf.si.bolnica.user.service.EmailService;
import raf.si.bolnica.user.service.LoginService;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;
import com.auth0.jwt.JWT;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.apache.tomcat.jni.Time.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raf.si.bolnica.user.constants.Constants.JWT_KEY;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class, JwtProperties.class})
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    LoginService loginService;
////    @InjectMocks
////    private LoginController loginController;

    private String jwt;

    private String jwtStatic = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsIm5hbWUiOiJhZG1pbiIsInN1cm5hbWUiOiJhZG1pbmljIiwidGl0bGUiOiJ0aXR1bGEiLCJwcm9mZXNzaW9uIjoiemFuaW1hbmplIiwiTEJaIjoiZTJiZjFkN2ItNGI2NC00MTNmLTg1MmItYWY4OTljZTBhMGFmIiwiUEJPIjoxMjM0NSwiZGVwYXJ0bWVudCI6IkhpcnVyZ2lqYSIsIlBCQiI6MTIzNCwiaG9zcGl0YWwiOiJLbGluacSNa28tYm9sbmnEjWtpIGNlbnRhciBcIkRyYWdpxaFhIE1pxaFvdmnEh1wiIiwicm9sZXMiOiJST0xFX0xBQk9SQVRPUklKU0tJX1RFSE5JQ0FSLFJPTEVfRFJfU1BFQ19QT1YsUk9MRV9NRURfU0VTVFJBLFJPTEVfU1BFQ0lKQUxJU1RBX01FRElDSU5TS0VfQklPSEVNSUpFLFJPTEVfVklTQV9NRURfU0VTVFJBLFJPTEVfTUVESUNJTlNLSV9CSU9IRU1JQ0FSLFJPTEVfQURNSU4sUk9MRV9EUl9TUEVDLFJPTEVfVklTSV9MQUJPUkFUT1JJSlNLSV9URUhOSUNBUixST0xFX0RSX1NQRUNfT0RFTEpFTkpBIiwiaXNzIjoiUW51UWJsUVduOEg5Z2dpd2ZHYkN4cFBBM2dkWTFvQWUiLCJleHAiOjE2NTU5NzA5MDl9.gyUz7SsFhnRw7nHQ24FWxT3U_wcrataZgzra3eK-gm8";


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

    @BeforeEach
    private void initJwt(){

        String secretKey = Base64.getEncoder().encodeToString("mysecret".getBytes());

        jwt = Jwts.builder()
                .setSubject("test@gmail.com")
                .claim("name","admin")
                .claim("surname", "adminic")
                .claim("roles", "ROLE_ADMIN,"+"ROLE_DR_SPEC_ODELJENJA,"+ "ROLE_DR_SPEC,"+ "ROLE_DR_SPEC_POV,"+
                                                                "ROLE_VISA_MED_SESTRA,"+ "ROLE_MED_SESTRA,"+"ROLE_VISI_LABORATORIJSKI_TEHNICAR,"+
                                                                "ROLE_LABORATORIJSKI_TEHNICAR,"+"ROLE_MEDICINSKI_BIOHEMICAR,"+
                                                                "ROLE_SPECIJALISTA_MEDICINSKE_BIOHEMIJE,"+ "ROLE_RECEPCIONER")
                .setIssuer(JWT_KEY)
                .setExpiration(new Date(new Date().getTime() +1000000000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }



//    @BeforeEach
//    void setUp() throws Exception {
//        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
//        loginRequestDTO.setEmail("test@gmail.com");
//        loginRequestDTO.setPassword("superadmin");
////        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
////        jwt = loginService.login(loginRequestDTO, req);
//
//        jwt = mockMvc.perform(post("/api/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(loginRequestDTO)))
//                .andReturn().getResponse().getContentAsString();
//
//        System.out.println(jwt);
//    }

    @Test
    void fetchUserByUsername() throws Exception {
//        System.out.println(jwt);
        mockMvc.perform(post("/api/fetch-user")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwt)
                        .param("username", "test@gmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void createEmployee() {
    }

    @Test
    void removeEmployee() {
    }

    @Test
    void getEmployee() {
    }

    @Test
    void listEmployeesByPbo() {
    }

    @Test
    void listEmployees() {
    }

    @Test
    void updateEmployee() {
    }
}
