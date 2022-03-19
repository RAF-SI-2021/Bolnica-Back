package raf.si.bolnica.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.service.UserService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedInUser loggedInUser;

    @GetMapping(value = "/fetch-user/{username}")
    public ResponseEntity<User> fetchAdminByUsername(@PathVariable("username") String username) {
        return ok(userService.fetchUserByEmail(username));
    }

    @GetMapping("/hello")
    public String hello() {
        System.out.println(loggedInUser.getRoles());
        return "Hello " + loggedInUser.getUsername();
    }
}
