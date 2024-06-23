package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.EmailRequest;
import com.daeseonbae.DSBBackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/verification")
    public String sendCode(@RequestBody EmailRequest emailRequest) {
        emailService.sendVerificationCode(emailRequest.getEmail());
        return "Verification code sent to " + emailRequest.getEmail();
    }
}
