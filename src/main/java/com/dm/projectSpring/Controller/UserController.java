package com.dm.projectSpring.Controller;

import com.dm.projectSpring.Entity.User;
import com.dm.projectSpring.Service.OtpService;
import com.dm.projectSpring.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        if (userService.existsByEmail(user.getEmail())) {
            response.put("message", "Email already registered.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        userService.saveUser(user);
        response.put("message", "Registration successful.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> user = userService.findByEmail(email);
        Map<String, Object> response = new HashMap<>();

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            response.put("userid", user.get().getUserid());
            response.put("accountType", user.get().getAccountType());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();

        if (email == null || email.isEmpty()) {
            response.put("message", "Email is required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!userService.existsByEmail(email)) {
            response.put("message", "Email not registered.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        otpService.sendOtpEmail(email);
        response.put("message", "OTP sent to your registered email.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        Map<String, String> response = new HashMap<>();

        if (email == null || email.isEmpty()) {
            response.put("message", "Email is required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (otp == null || otp.isEmpty()) {
            response.put("message", "OTP is required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            response.put("message", "OTP verified successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid or expired OTP.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        Map<String, String> response = new HashMap<>();
        if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            response.put("message", "Email and password are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setPassword(newPassword); // Update password
            userService.saveUser(existingUser);

            response.put("message", "Password reset successful.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid reset request.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
