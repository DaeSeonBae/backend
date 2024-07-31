package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.service.LogoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    private final LogoutService logoutService;

    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    // 로그아웃 요청을 처리하는 엔드포인트
    @PostMapping("/users/sign-out")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        // 로그아웃 서비스 호출하여 토큰 무효화
        if (logoutService.logout(token)) {
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid token");
        }
    }
}
