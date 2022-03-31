package raf.si.bolnica.user.controllers;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.query.SearchCriteria;
import raf.si.bolnica.user.query.UserSpecification;
import raf.si.bolnica.user.requests.CreateEmployeeRequestDTO;
import raf.si.bolnica.user.requests.ListEmployeesRequestDTO;
import raf.si.bolnica.user.requests.UpdateEmployeeRequestDTO;
import raf.si.bolnica.user.responses.UserResponseDTO;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;
import raf.si.bolnica.user.service.EmailService;


import javax.swing.text.html.HTMLDocument;
import javax.websocket.server.PathParam;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserExceptionHandler userExceptionHandler;

    @Autowired
    private OdeljenjeService odeljenjeService;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/fetch-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> fetchAdminByUsername(@RequestParam String username) {
        User user = userService.fetchUserByEmail(username);
        if (user != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getName(), user.getSurname(), user.getPassword(), user.getEmail(), user.getRoles());
            return ok(userResponseDTO);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword() {

        User user = userService.fetchUserByEmail(loggedInUser.getUsername());

        if (user == null) {
            // Username doesn't exist, return 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // Username exists, proceeds to generate and save new password, also send email
            String generatedPassword = userService.generateNewPassword(user);

            userService.savePassword(user, generatedPassword);
            emailService.sendEmail(loggedInUser.getUsername(), generatedPassword);

            return ok().build();
        }

    }

    @PostMapping(value = Constants.CREATE_EMPLOYEE)
    public ResponseEntity<?> createEmployee(@RequestBody CreateEmployeeRequestDTO requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            Odeljenje odeljenje = odeljenjeService.fetchOdeljenjeById(requestDTO.getDepartment());
            String username = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));
            String password = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));

            userExceptionHandler.validateUsername.accept(username);
            userExceptionHandler.validateUserTitle.accept(requestDTO.getTitle());
            userExceptionHandler.validateUserProfession.accept(requestDTO.getProfession());
            userExceptionHandler.validateUserGender.accept(requestDTO.getGender());

            User user = new User();

            user.setOdeljenje(odeljenje);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setKorisnickoIme(username);
            user.setEmail(requestDTO.getEmail());
            user.setName(requestDTO.getName());
            user.setSurname(requestDTO.getSurname());
            user.setAdresaStanovanja(requestDTO.getAddress());
            user.setDatumRodjenja(requestDTO.getDob());
            user.setMestoStanovanja(requestDTO.getCity());
            user.setJmbg(requestDTO.getJmbg());
            user.setKontaktTelefon(requestDTO.getContact());
            user.setPol(requestDTO.getGender());
            user.setTitula(requestDTO.getTitle());
            user.setZanimanje(requestDTO.getProfession());

            User userToReturn = userService.createEmployee(user);
            return ok(userToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @DeleteMapping(value = Constants.REMOVE_EMPLOYEE)
    public ResponseEntity<?> removeEmployee(@RequestParam String username) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {

            User user = userService.fetchUserByUsername(username);

            if (user == null) {
                return ResponseEntity.status(403).build();
            }

            if (user.getEmail().equals(loggedInUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            userService.deleteById(user.getUserId());
            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = Constants.GET_EMPLOYEE)
    public ResponseEntity<UserResponseDTO> getEmployee(@PathVariable Long lbz) {

        User user = userService.fetchUserByLBZ(lbz);


        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }



        if (loggedInUser.getRoles().contains("ROLE_ADMIN") || loggedInUser.getLBZ().equals(lbz)) {
            System.out.println("test");
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getName(), user.getSurname(), "", user.getEmail(), user.getRoles());
            return ok(userResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(value = Constants.LIST_EMPLOYEES)
    public ResponseEntity<List<UserResponseDTO>> listEmployees(@RequestBody ListEmployeesRequestDTO requestDTO) {
        Specification spec = null;
        if(requestDTO.getName()!=null) {
            UserSpecification nameSpecification = new UserSpecification(new SearchCriteria("name",requestDTO.getName()));
            if(spec != null) {
                spec = Specification.where(nameSpecification);
            }
            else {
                spec = Specification.where(spec).and(nameSpecification);
            }
        }
        if(requestDTO.getSurname()!=null) {
            UserSpecification surnameSpecification = new UserSpecification(new SearchCriteria("surname",requestDTO.getSurname()));
            if(spec != null) {
                spec = Specification.where(surnameSpecification);
            }
            else {
                spec = Specification.where(spec).and(surnameSpecification);
            }
        }
        if(requestDTO.getObrisan()!=null) {
            UserSpecification obrisanSpecification = new UserSpecification(new SearchCriteria("obrisan",requestDTO.getObrisan()));
            if(spec != null) {
                spec = Specification.where(obrisanSpecification);
            }
            else {
                spec = Specification.where(spec).and(obrisanSpecification);
            }
        }
        if(requestDTO.getDepartment()!=null) {
            UserSpecification odeljenjeSpecification = new UserSpecification(new SearchCriteria("odeljenje",odeljenjeService.fetchOdeljenjeById(requestDTO.getDepartment())));
            if(spec != null) {
                spec = Specification.where(odeljenjeSpecification);
            }
            else {
                spec = Specification.where(spec).and(odeljenjeSpecification);
            }
        }
        List<User> users = userService.filterUsers(spec);
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for(User user: users) {
            userResponseDTOList.add(new UserResponseDTO(user.getUserId(), user.getName(), user.getSurname(), "", user.getEmail(), user.getRoles()));
        }
        return ok(userResponseDTOList);
    }

    @PutMapping(value = Constants.UPDATE_EMPLOYEE)
    public ResponseEntity<?> updateEmployee(@RequestBody UpdateEmployeeRequestDTO requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            Odeljenje odeljenje = odeljenjeService.fetchOdeljenjeById(requestDTO.getDepartment());
            String username = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));
            String password = requestDTO.getNewPassword();

            userExceptionHandler.validateUsername.accept(username);
            userExceptionHandler.validateUserTitle.accept(requestDTO.getTitle());
            userExceptionHandler.validateUserProfession.accept(requestDTO.getProfession());
            userExceptionHandler.validateUserGender.accept(requestDTO.getGender());

            User user = userService.fetchUserByLBZ(requestDTO.getLbz());

            if(user == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }



            user.setOdeljenje(odeljenje);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setKorisnickoIme(username);
            user.setEmail(requestDTO.getEmail());
            user.setName(requestDTO.getName());
            user.setSurname(requestDTO.getSurname());
            user.setAdresaStanovanja(requestDTO.getAddress());
            user.setDatumRodjenja(requestDTO.getDob());
            user.setMestoStanovanja(requestDTO.getCity());
            user.setJmbg(requestDTO.getJmbg());
            user.setKontaktTelefon(requestDTO.getContact());
            user.setPol(requestDTO.getGender());
            user.setTitula(requestDTO.getTitle());
            user.setZanimanje(requestDTO.getProfession());

            User userToReturn = userService.createEmployee(user);
            return ok(userToReturn);
        }
        else {
            User requestUser = userService.fetchUserByLBZ(requestDTO.getLbz());
            if(loggedInUser.getLBZ().equals(requestUser.getLicniBrojZaposlenog())) {
                User user = userService.fetchUserByLBZ(requestDTO.getLbz());
                if(requestDTO.getContact()!=null) {
                    user.setKontaktTelefon(requestDTO.getContact());
                }
                if(requestDTO.getNewPassword()!=null && user.getPassword().equals(BCrypt.hashpw(requestDTO.getOldPassword(), BCrypt.gensalt()))) {
                    user.setPassword(BCrypt.hashpw(requestDTO.getNewPassword(), BCrypt.gensalt()));
                }
                user = userService.createEmployee(user);
                return ok(user);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
