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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import raf.si.bolnica.management.ManagementApplication;
import raf.si.bolnica.management.configuration.SpringWebConfiguration;
import raf.si.bolnica.management.jwt.JwtProperties;

import javax.servlet.ServletContext;
import java.lang.reflect.Type;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringWebConfiguration.class, JwtProperties.class})
@SpringBootTest(classes = ManagementApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    public String jwt;

    private Gson g;

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
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

        /*
        *
        * Jwt has to be used for all methods. For example:
        *
        * jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsIm5hbWUiOiJhZG1pbiIsInN1cm5hbWUiOiJhZG1pbmljIiwidGl0bGUiOiJ0aXR1bGEiLCJwcm9mZXNzaW9uIjoiemFuaW1hbmplIiwiTEJaIjoiMjhhYWI4NmMtZDQwZi00ZTZmLTljZDEtN2RhNTU3Nzk4NzdmIiwiUEJPIjoxMjM0NSwiZGVwYXJ0bWVudCI6IkhpcnVyZ2lqYSIsIlBCQiI6MTIzNCwiaG9zcGl0YWwiOiJLbGluacSNa28tYm9sbmnEjWtpIGNlbnRhciBcIkRyYWdpxaFhIE1pxaFvdmnEh1wiIiwicm9sZXMiOiJST0xFX0RSX1NQRUNfUE9WLFJPTEVfTUVEX1NFU1RSQSxST0xFX0RSX1NQRUMsUk9MRV9NRURJQ0lOU0tJX0JJT0hFTUlDQVIsUk9MRV9EUl9TUEVDX09ERUxKRU5KQSxST0xFX1ZJU0lfTEFCT1JBVE9SSUpTS0lfVEVITklDQVIsUk9MRV9SRUNFUENJT05FUixST0xFX0FETUlOLFJPTEVfU1BFQ0lKQUxJU1RBX01FRElDSU5TS0VfQklPSEVNSUpFLFJPTEVfVklTQV9NRURfU0VTVFJBLFJPTEVfTEFCT1JBVE9SSUpTS0lfVEVITklDQVIiLCJpc3MiOiJRbnVRYmxRV244SDlnZ2l3ZkdiQ3hwUEEzZ2RZMW9BZSIsImV4cCI6MTY1NjkyMTM4OH0.a_tIzTRuKuq_5oqUDO5ygParQXcg5ZBra03HFwREdVs";
        *
        * */



    }

    @AfterAll
    public void cleanup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    }
}
