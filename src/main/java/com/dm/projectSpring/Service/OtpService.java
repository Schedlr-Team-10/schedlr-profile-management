package com.dm.projectSpring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    public void sendOtpEmail(String email) {
        String otp = generateOtp();
        otpStore.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);

        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {
        if (otpStore.containsKey(email) && otpStore.get(email).equals(otp)) {
            otpStore.remove(email); // OTP is valid and can be removed
            return true;
        }
        return false;
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
    