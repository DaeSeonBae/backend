package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class LogoutService {

    // 블랙리스트로 사용할 Set
    private final Set<String> blacklistedTokens = new HashSet<>();
    private final JWTUtil jwtUtil;

    public LogoutService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 로그아웃 메소드: 토큰을 블랙리스트에 추가
    public boolean logout(String token) {
        // Bearer prefix 제거
        String jwt = token.replace("Bearer ", "");
        return blacklistedTokens.add(jwt);
    }

    // 토큰이 블랙리스트에 있는지 확인
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // 주기적으로 블랙리스트에서 만료된 토큰을 제거
    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void removeExpiredTokens() {
        Iterator<String> iterator = blacklistedTokens.iterator();
        while (iterator.hasNext()) {
            String token = iterator.next();
            if (jwtUtil.isExpired(token)) {
                iterator.remove();
            }
        }
    }
}
