package com.dm.projectSpring.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.projectSpring.Entity.User;
import com.dm.projectSpring.Service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/schedlr")
public class UserController {
    @Autowired
    private UserService userService;

   @PostMapping("/register")
public ResponseEntity<User> registerUser(@RequestBody User user) {
    if (user.getAccountType() == null) {
        user.setAccountType(User.AccountType.PERSONAL); // Default role
    }
    User registeredUser = userService.registerUser(user);
    return ResponseEntity.ok(registeredUser);
}


    @PostMapping("/login")
    public ResponseEntity<Optional<User>> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> user = userService.loginUser(email, password);
        if (user.isPresent()) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    
}