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
}
