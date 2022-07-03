import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.util.ReflectionTestUtils;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.jwt.JwtProperties;
import raf.si.bolnica.user.jwt.JwtTokenProvider;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.properties.EmailProperties;
import raf.si.bolnica.user.repositories.OdeljenjeRepository;
import raf.si.bolnica.user.repositories.UserRepository;
import raf.si.bolnica.user.repositories.ZdravstvenaUstanovaRepository;
import raf.si.bolnica.user.requests.LoginRequestDTO;
import raf.si.bolnica.user.service.EmailServiceImpl;
import raf.si.bolnica.user.service.LoginServiceImpl;
import raf.si.bolnica.user.service.OdeljenjeServiceImpl;
import raf.si.bolnica.user.service.UserServiceImpl;

import javax.mail.AuthenticationFailedException;
import javax.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.util.*;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServicesTests {

    @Mock
    EmailProperties emailProperties;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    OdeljenjeRepository odeljenjeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ZdravstvenaUstanovaRepository zdravstvenaUstanovaRepository;

    @Mock
    UserExceptionHandler userExceptionHandler;

    @InjectMocks
    EmailServiceImpl emailService;

    @InjectMocks
    LoginServiceImpl loginService;

    @InjectMocks
    OdeljenjeServiceImpl odeljenjeService;

    @InjectMocks
    UserServiceImpl userService;

    @InjectMocks
    JwtTokenProvider jwtTokenProviderInject;

    @Mock
    JwtProperties jwtProperties;

    @Test
    public void sendEmailTest() {
        when(emailProperties.getEmail()).thenReturn("mejl");
        when(emailProperties.getPassword()).thenReturn("sifra");
        emailService.sendEmail("username", "pass");
    }

    @Test
    public void loginTest() {
        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler, "validateUserUsername", handler.validateUserUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validatePassword", handler.validatePassword);

        when(userRepository.findByEmail(any(String.class))).thenAnswer(i -> {
            User u = new User();
            u.setPassword(BCrypt.hashpw("pass", BCrypt.gensalt()));
            u.setEmail((String) i.getArguments()[0]);
            return u;
        });

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("mejl");
        loginRequestDTO.setPassword("pass");

        when(jwtTokenProvider.createToken(any(String.class), any(User.class))).thenReturn("token");

        String token = loginService.login(loginRequestDTO, mock(HttpServletRequest.class));

        assertThat(token).isEqualTo("token");
    }

    @Test
    public void OdeljenjeTests() {
        when(odeljenjeRepository.findByOdeljenjeId(eq(1L))).thenAnswer(i -> {
            Odeljenje o = new Odeljenje();
            o.setOdeljenjeId(1L);
            return o;
        });

        Odeljenje o = odeljenjeService.fetchOdeljenjeById(1L);
        assertThat(o.getOdeljenjeId()).isEqualTo(1L);

        List<Odeljenje> lista = odeljenjeService.findAll();
        assertThat(lista.size()).isEqualTo(0);
    }

    @Test
    public void jwtTest() {
        User user1 = new User();
        user1.setEmail("test1@gmail.com");
        user1.setPassword(BCrypt.hashpw("superadmin", BCrypt.gensalt()));
        user1.setName("Super");
        user1.setSurname("Admin");
        Set<Role> roles = new HashSet<>();
        Role drSpecRole = new Role();
        drSpecRole.setName("ROLE_DR_SPEC");
        roles.add(drSpecRole);
        user1.setRoles(roles);

        ZdravstvenaUstanova zdravstvenaUstanova = new ZdravstvenaUstanova();
        zdravstvenaUstanova.setAdresa("Heroja Milana Tepića 1, Beograd");
        zdravstvenaUstanova.setDatumOsnivanja(new Date(Calendar.getInstance().getTime().getTime()));
        zdravstvenaUstanova.setDelatnost("Ginekologija i akušerstvo");
        zdravstvenaUstanova.setMesto("Beograd");
        zdravstvenaUstanova.setNaziv("Kliničko-bolnički centar \"Dragiša Mišović\"");
        zdravstvenaUstanova.setPoslovniBrojBolnice(1234);
        zdravstvenaUstanova.setSkracenNaziv("KBC Dragiša Mišović");

        Odeljenje odeljenje = new Odeljenje();
        odeljenje.setNaziv("Hirurgija");
        odeljenje.setBolnica(zdravstvenaUstanova);
        odeljenje.setPoslovniBrojOdeljenja(12345);
        user1.setOdeljenje(odeljenje);

        //admin user fields
        user1.setLbz(UUID.randomUUID());
        user1.setName("admin");
        user1.setSurname("adminic");
        user1.setDatumRodjenja(new Date(System.currentTimeMillis()));
        user1.setPol("Muski");
        user1.setJmbg("123456789");
        user1.setAdresaStanovanja("adresa 1");
        user1.setMestoStanovanja("SRBIJA");
        user1.setTitula("titula");
        user1.setKorisnickoIme("superadminn");
        user1.setZanimanje("zanimanje");

        when(jwtProperties.getValidityInMilliseconds()).thenAnswer(i -> 10L);
        String token = jwtTokenProviderInject.createToken("a", user1);

        assertThat(token).isNotEmpty();
    }

    @Test
    public void OdeljenjeTestsFindAll() {
        when(odeljenjeRepository.findAll()).thenAnswer(i -> {
            return new ArrayList<>();
        });

        List<Odeljenje> odeljenjes = odeljenjeService.findAllByPbb();
        assertThat(odeljenjes).isNotNull();
    }

    @Test
    public void OdeljenjeTestsFindAllHospitals() {
        when(zdravstvenaUstanovaRepository.findAll()).thenAnswer(i -> {
            return new ArrayList<>();
        });

        List<ZdravstvenaUstanova> odeljenjes = odeljenjeService.findAllHospitals();
        assertThat(odeljenjes).isNotNull();
    }

    @Test
    public void OdeljenjeTestsSaveOdeljenje() {
        when(odeljenjeRepository.save(any(Odeljenje.class))).thenAnswer(i -> {
            return new Odeljenje();
        });

        Odeljenje odeljenje = odeljenjeService.saveOdeljenje(new Odeljenje());
        assertThat(odeljenje).isNotNull();
    }

    @Test
    public void OdeljenjeTestsSearch() {
        when(odeljenjeRepository.findByNazivContaining(any(String.class))).thenAnswer(i -> {
            return new ArrayList<>();
        });

        List<Odeljenje> odeljenjes = odeljenjeService.searchByNaziv("a");
        assertThat(odeljenjes).isNotNull();
    }

    @Test
    public void generatePasswordTest() {
        User u = new User();
        String newPassword = userService.generateNewPassword(u);
        assertThat(newPassword.length()).isEqualTo(8);
    }

    @Test
    public void savePasswordTest() {
        User u = new User();
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        User newUser = userService.savePassword(u, "pass");
        assertThat(BCrypt.checkpw("pass", newUser.getPassword())).isTrue();
    }

//    @Test
//    public void fetchNacelnikOdeljenjaTest() {
//        User u = new User();
//        List<User> response = new ArrayList<>();
//        response.add(u);
//        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> new Odeljenje());
//        when(userRepository.findByOdeljenje(any(Odeljenje.class))).thenAnswer(i -> response);
//        User newUser = userService.fetchNacelnikOdeljenja(1L);
//        assertThat(newUser).isNotNull();
//    }
//
//    @Test
//    public void fetchUsersByPBO() {
//        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> new Odeljenje());
//        List<User> users = userService.fetchUsersByPBO(1L);
//        assertThat(users).isNotNull();
//    }
}
