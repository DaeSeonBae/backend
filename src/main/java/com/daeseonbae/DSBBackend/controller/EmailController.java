package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.EmailRequest;
import com.daeseonbae.DSBBackend.dto.VerificationRequest;
import com.daeseonbae.DSBBackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/api/verification")
    public String sendCode(@RequestBody EmailRequest emailRequest) {
        emailService.sendVerificationCode(emailRequest.getEmail());
        return "Verification code sent to " + emailRequest.getEmail();
    }

    @PostMapping("/api/verification/check")
    public String checkCode(@RequestBody VerificationRequest verificationRequest) {
        boolean isValid = emailService.verifyCode(verificationRequest.getEmail(), verificationRequest.getCode());
        return isValid ? "Verification successful" : "Verification failed";
    }
}
