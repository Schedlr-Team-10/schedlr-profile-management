package com.dm.projectSpring.Controller;

import com.dm.projectSpring.Entity.User;
import com.dm.projectSpring.Service.OtpService;
import com.dm.projectSpring.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/schedlr")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        if (userService.existsByEmail(user.getEmail())) {
            response.put("message", "Email already registered.");
            return response;
        }

        userService.saveUser(user);
        response.put("message", "Registration successful.");
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> user = userService.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            response.put("userid", user.get().getUserid());
            response.put("accountType", user.get().getAccountType());
        } else {
            response.put("message", "Invalid email or password.");
        }

        return response;
    }

    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        if (!userService.existsByEmail(email)) {
            response.put("message", "Email not registered.");
            return response;
        }

        otpService.sendOtpEmail(email);
        response.put("message", "OTP sent to your email.");
        return response;
    }

    @PostMapping("/verify-otp")
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        Map<String, String> response = new HashMap<>();

        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            response.put("message", "OTP verified successfully.");
        } else {
            response.put("message", "Invalid OTP.");
        }

        return response;
    }
}
