import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import raf.si.bolnica.user.controllers.UserController;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.requests.CreateEmployeeRequestDTO;
import raf.si.bolnica.user.requests.ListEmployeesRequestDTO;
import raf.si.bolnica.user.service.EmailService;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    UserService userService;

    @Mock
    private EmailService emailService;

    @Mock
    EntityManager entityManager;

    public User getUser(){
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

        user1.setOdeljenje(odeljenje);

        //admin user fields
        user1.setLicniBrojZaposlenog(123);
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

        return user1;
    }

    @Test
    void forgotPassword(){
        User u = getUser();

        when(loggedInUser.getUsername()).thenReturn(u.getEmail());

        when(userService.fetchUserByEmail(u.getEmail())).thenReturn(u);

        String pas = "123";
        when(userService.generateNewPassword(u)).thenReturn(pas);

        when(emailService.sendEmail(loggedInUser.getUsername(), pas)).thenReturn(true);

        ResponseEntity<?> responseForgot = userController.forgotPassword();

        assertThat(responseForgot.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void removeEmployee(){
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        User u = getUser();

        String toDelete = u.getKorisnickoIme();

        when(userService.fetchUserByUsername(u.getKorisnickoIme())).thenReturn(u);

        ResponseEntity<?> response = userController.removeEmployee(toDelete);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void listEmployees(){
        User u = getUser();

        ListEmployeesRequestDTO requestDTO = new ListEmployeesRequestDTO();

        List<User> users = new ArrayList<>();

        users.add(u);

        TypedQuery query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(new LinkedList<>());
        when(entityManager.createQuery(any(String.class),any(Class.class))).thenReturn(query);

        when(query.getResultList()).thenReturn(users);

        ResponseEntity<?> response = userController.listEmployees(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}