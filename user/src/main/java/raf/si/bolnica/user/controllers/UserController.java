package raf.si.bolnica.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.responses.UserResponseDTO;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;
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

    @GetMapping(value = "/fetch-user/{username}")
    public ResponseEntity<UserResponseDTO> fetchAdminByUsername(@PathVariable("username") String username) {
        User user = userService.fetchUserByEmail(username);
        if (user != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getName(), user.getSurname(), user.getPassword(), user.getEmail(), user.getRoles());
            return ok(userResponseDTO);
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/hello")
    public String hello() {
        System.out.println(loggedInUser.getRoles());
        return "Hello " + loggedInUser.getUsername();
    }

    @PostMapping(value = Constants.CREATE_EMPLOYEE)
    public ResponseEntity<?> createEmployee(@RequestBody User requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            Odeljenje odeljenje = odeljenjeService.fetchOdeljenjeById(requestDTO.getOdeljenje().getIdOdeljenja());
            String username = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));
            String password = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));

            requestDTO.setOdeljenje(odeljenje);
            requestDTO.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            requestDTO.setKorisnickoIme(username);

            userExceptionHandler.validateUsername.accept(requestDTO.getKorisnickoIme());
            userExceptionHandler.validateUserTitle.accept(requestDTO.getTitula());
            userExceptionHandler.validateUserProfession.accept(requestDTO.getZanimanje());

            User userToReturn = userService.createEmployee(requestDTO);
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(500).build();
    }

}
