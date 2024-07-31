package com.daeseonbae.DSBBackend.jwt;

import com.daeseonbae.DSBBackend.config.SecurityConfig;
import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.service.LogoutService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//검증 필터
//요청에 대해서 한번만 동작함
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final LogoutService logoutService;

    public JWTFilter(JWTUtil jwtUtil, LogoutService logoutService) {
        this.jwtUtil = jwtUtil;
        this.logoutService = logoutService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증(null 또는 Bearer값이 없다면 종료)
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); //해당 필터를 종료하고 다음 필터네 req,res 를 넘김

            return;
        }

        String token = authorization.split(" ")[1]; //Bearer 분리
        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token) || logoutService.isTokenBlacklisted(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        Integer id = jwtUtil.getId(token);
        String email = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        String nickName = jwtUtil.getNickName(token);
        String department = jwtUtil.getDepartment(token);

        //유저 초기화 진행
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setEmail(email);
        userEntity.setNickname(nickName);
        userEntity.setDepartment(department);
        //비밀번호값의 경우 토큰에 존재 하지 않음 따라서 매번 DB요청을 방지를 위해 임의로 작성
        userEntity.setPassword("1234");
        userEntity.setRole(role);

        //회원정보 객체에 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        //사용자의 정보,사용자 패스워드(JWT는 포함 x),사용자의 권한 정보 --> 인증 처리후 인가 권한 부여
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails,null,customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
