package com.daeseonbae.DSBBackend.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

//JWT 검증 및 발급
@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret){
        //객체 키를 생성
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }


    //세개의 검증 메소드
    public String getUsername(String token){
        //verifyWith -->  JWT의 서명을 검증하기 위해 사용되는 비밀 키를 설정, 다. 토큰을 생성할 때 사용된 키와 일치해야함
        //parseSignedClaims(toekn) -->  주어진 토큰을 파싱하고 검증함 , 서명이 유효한지 확인하고 토큰의 클레임(claim)을 추출
        // 이후 페이로드를 반환하고 특정 클레임을 string 형태로 추출함
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token){
        //위와 동일한 원리로 role 값을 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public boolean isExpired(String token){
        //getExpiration --> JWT의 만료시간을 가져옴 이후 현재시간과 비교하여 만료되면 true를 반환
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }




    //토큰을 생성
    public String createJwt(String email,String role,Long expiredMs){
        return Jwts.builder()
                .claim("email",email)
                .claim("role",role)
                .issuedAt(new Date(System.currentTimeMillis()))  //현재 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) //만료시간 설정
                .signWith(secretKey) //암호화
                .compact();
    }

}
