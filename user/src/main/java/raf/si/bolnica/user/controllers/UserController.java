package raf.si.bolnica.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.service.UserService;
import raf.si.bolnica.user.service.EmailService;


import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/fetch-user/{username}")
    public ResponseEntity<User> fetchAdminByUsername(@PathVariable("username") String username) {
        return ok(userService.fetchUserByEmail(username));
    }

    @GetMapping("/hello")
    public String hello() {
        System.out.println(loggedInUser.getRoles());
        return "Hello " + loggedInUser.getUsername();
    }

    @PostMapping("/forgot-password")
    public HttpStatus forgotPassword(@RequestBody String usersEmail) {

        User user = userService.fetchUserByEmail(usersEmail);

        System.out.println("ovo je username : " + usersEmail);

        if (user == null) {
            //username doesnt exist, return 403
            return HttpStatus.valueOf(403);
        } else {
            //username exists, proceeds to generate and save new password, also send email
            String generatedPassword = userService.generateNewPassword(user);
            userService.savePassword(user, generatedPassword);
            emailService.sendEmail(usersEmail, generatedPassword);
            return HttpStatus.valueOf(200);
        }

    }
}
