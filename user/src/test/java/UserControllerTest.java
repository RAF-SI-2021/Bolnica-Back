import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.util.ReflectionTestUtils;
import raf.si.bolnica.user.controllers.UserController;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.Role;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.models.ZdravstvenaUstanova;
import raf.si.bolnica.user.repositories.RoleRepository;
import raf.si.bolnica.user.requests.CreateEmployeeRequestDTO;
import raf.si.bolnica.user.requests.ListEmployeesRequestDTO;
import raf.si.bolnica.user.requests.UpdateEmployeeRequestDTO;
import raf.si.bolnica.user.responses.UserDataResponseDTO;
import raf.si.bolnica.user.service.EmailService;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    LoggedInUser loggedInUser;

    @Mock
    UserService userService;

    @Mock
    OdeljenjeService odeljenjeService;

    @Mock
    private EmailService emailService;

    @Mock
    UserExceptionHandler userExceptionHandler;

    @InjectMocks
    UserController userController;

    @Mock
    EntityManager entityManager;

    @Mock
    RoleRepository roleRepository;

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

        return user1;
    }

    public CreateEmployeeRequestDTO createEmployeeRequestDTO() {
        CreateEmployeeRequestDTO requestDTO = new CreateEmployeeRequestDTO();
        requestDTO.setEmail("email@email.com");
        requestDTO.setName("name");
        requestDTO.setSurname("surname");
        requestDTO.setJmbg("jmbg");
        requestDTO.setContact("kontakt");
        requestDTO.setAddress("adresa");
        requestDTO.setCity("grad");
        requestDTO.setDepartment(1);
        requestDTO.setDob(Date.valueOf(LocalDate.now().minusYears(40)));
        requestDTO.setGender("male");
        requestDTO.setTitle("Dipl. farm.");
        requestDTO.setProfession("Spec. hematolog");
        return requestDTO;
    }

    public UpdateEmployeeRequestDTO updateEmployeeRequestDTO() {
        UpdateEmployeeRequestDTO requestDTO = new UpdateEmployeeRequestDTO();
        requestDTO.setEmail("email@email.com");
        requestDTO.setName("name");
        requestDTO.setSurname("surname");
        requestDTO.setJmbg("jmbg");
        requestDTO.setContact("kontakt");
        requestDTO.setAddress("adresa");
        requestDTO.setCity("grad");
        requestDTO.setDepartment(1);
        requestDTO.setDob(Date.valueOf(LocalDate.now().minusYears(40)));
        requestDTO.setGender("male");
        requestDTO.setTitle("Dipl. farm.");
        requestDTO.setProfession("Spec. hematolog");
        requestDTO.setOldPassword("stari");
        requestDTO.setNewPassword("novi");
        requestDTO.setLbz(UUID.randomUUID());
        return requestDTO;
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

        when(userService.fetchUserByLBZ(u.getLbz())).thenReturn(u);

        ResponseEntity<?> response = userController.removeEmployee(u.getLbz().toString());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void createEmployeeSuccess() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.saveEmployee(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        CreateEmployeeRequestDTO requestDTO = createEmployeeRequestDTO();

        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> {
            Odeljenje odeljenje = new Odeljenje();
            odeljenje.setOdeljenjeId((Long)i.getArguments()[0]);
            return odeljenje;
        });

        when(roleRepository.findByName(any(String.class))).thenReturn(new Role());

        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUsername",handler.validateUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserGender",handler.validateUserGender);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserTitle", handler.validateUserTitle);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserProfession",handler.validateUserProfession);

        ResponseEntity<?> response = userController.createEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(UserDataResponseDTO.class);

        UserDataResponseDTO data = (UserDataResponseDTO) response.getBody();

        assertThat(data).isNotNull();

        assertThat(data.getAddress()).isEqualTo(requestDTO.getAddress());
        assertThat(data.getCity()).isEqualTo(requestDTO.getCity());
        assertThat(data.getContact()).isEqualTo(requestDTO.getContact());
        assertThat(data.getCity()).isEqualTo(requestDTO.getCity());
        assertThat(data.getEmail()).isEqualTo(requestDTO.getEmail());
        assertThat(data.getLbz()).isNotNull();
        assertThat(data.getDob()).isEqualTo(requestDTO.getDob());
        assertThat(data.getDepartment()).isEqualTo(requestDTO.getDepartment());
        assertThat(data.getGender()).isEqualTo(requestDTO.getGender());
        assertThat(data.getName()).isEqualTo(requestDTO.getName());
        assertThat(data.getJmbg()).isEqualTo(requestDTO.getJmbg());
        assertThat(data.getSurname()).isEqualTo(requestDTO.getSurname());
        assertThat(data.getProfession()).isEqualTo(requestDTO.getProfession());
        assertThat(data.getTitle()).isEqualTo(requestDTO.getTitle());
    }

    @Test
    void createEmployeeUnauthorized() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_MED_SESTRA");

        when(loggedInUser.getRoles()).thenReturn(roles);

        ResponseEntity<?> response = userController.createEmployee(createEmployeeRequestDTO());

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void adminUpdateEmployeeNoSuchUser() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.fetchUserByLBZ(any(UUID.class))).thenReturn(null);

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> {
            Odeljenje odeljenje = new Odeljenje();
            odeljenje.setOdeljenjeId((Long)i.getArguments()[0]);
            return odeljenje;
        });

        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUsername",handler.validateUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserGender",handler.validateUserGender);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserTitle", handler.validateUserTitle);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserProfession",handler.validateUserProfession);

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);
    }

    @Test
    void adminUpdateEmployeeSuccess() {
        Set<String> roles = new TreeSet<>();

        roles.add("ROLE_ADMIN");

        when(loggedInUser.getRoles()).thenReturn(roles);

        when(userService.fetchUserByLBZ(any(UUID.class))).thenReturn(new User());

        when(userService.saveEmployee(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UpdateEmployeeRequestDTO requestDTO = updateEmployeeRequestDTO();

        when(odeljenjeService.fetchOdeljenjeById(any(Long.class))).thenAnswer(i -> {
            Odeljenje odeljenje = new Odeljenje();
            odeljenje.setOdeljenjeId((Long)i.getArguments()[0]);
            return odeljenje;
        });

        UserExceptionHandler handler = new UserExceptionHandler();

        ReflectionTestUtils.setField(userExceptionHandler,"validateUsername",handler.validateUsername);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserGender",handler.validateUserGender);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserTitle", handler.validateUserTitle);
        ReflectionTestUtils.setField(userExceptionHandler, "validateUserProfession",handler.validateUserProfession);

        ResponseEntity<?> response = userController.updateEmployee(requestDTO);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        assertThat(response.getBody()).isInstanceOf(User.class);

        User data = (User) response.getBody();

        assertThat(data).isNotNull();

        assertThat(data.getAdresaStanovanja()).isEqualTo(requestDTO.getAddress());
        assertThat(data.getMestoStanovanja()).isEqualTo(requestDTO.getCity());
        assertThat(data.getKontaktTelefon()).isEqualTo(requestDTO.getContact());
        assertThat(data.getEmail()).isEqualTo(requestDTO.getEmail());
        assertThat(data.getDatumRodjenja()).isEqualTo(requestDTO.getDob());
        assertThat(data.getOdeljenje().getOdeljenjeId()).isEqualTo(requestDTO.getDepartment());
        assertThat(data.getPol()).isEqualTo(requestDTO.getGender());
        assertThat(data.getName()).isEqualTo(requestDTO.getName());
        assertThat(data.getJmbg()).isEqualTo(requestDTO.getJmbg());
        assertThat(data.getSurname()).isEqualTo(requestDTO.getSurname());
        assertThat(data.getZanimanje()).isEqualTo(requestDTO.getProfession());
        assertThat(data.getTitula()).isEqualTo(requestDTO.getTitle());
    }

//    @Test
//    void listEmployees(){
//        User u = getUser();
//
//        ListEmployeesRequestDTO requestDTO = new ListEmployeesRequestDTO();
//
//        List<User> users = new ArrayList<>();
//
//        users.add(u);
//
//        TypedQuery query = mock(TypedQuery.class);
//        when(query.getResultList()).thenReturn(new LinkedList<>());
//        when(entityManager.createQuery(any(String.class),any(Class.class))).thenReturn(query);
//
//        when(query.getResultList()).thenReturn(users);
//
//        ResponseEntity<?> response = userController.listEmployees(requestDTO, 1, 5);
//
//        assertThat(response.getStatusCodeValue()).isEqualTo(200);
//    }
}