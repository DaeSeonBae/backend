package com.daeseonbae.DSBBackend.domain.controller;

import com.daeseonbae.DSBBackend.domain.dto.VerificationRequest;
import com.daeseonbae.DSBBackend.domain.dto.user.EmailRequest;
import com.daeseonbae.DSBBackend.domain.dto.user.JoinDTO;
import com.daeseonbae.DSBBackend.domain.service.EmailService;
import com.daeseonbae.DSBBackend.domain.service.JoinService;
import com.daeseonbae.DSBBackend.global.api.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> joinUser(@RequestBody @Valid JoinDTO joinDTO){
        joinService.joinUser(joinDTO);
        return ResponseEntity.ok(BaseResponse.success());
    }

    @PostMapping("/email")
    public ResponseEntity<BaseResponse<?>> sendCode(@RequestBody EmailRequest emailRequest){
        emailService.sendVerificationCode(emailRequest.getEmail());
        return ResponseEntity.ok(BaseResponse.success("이메일 전송 완료!"));
    }

    @PostMapping("/email/check")
    public ResponseEntity<BaseResponse<Void>> checkCode(@RequestBody VerificationRequest verificationRequest){
        emailService.verifyCode(verificationRequest.getEmail(), verificationRequest.getCode());
        return ResponseEntity.ok(BaseResponse.success());
    }
}
