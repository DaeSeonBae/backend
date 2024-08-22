package com.daeseonbae.DSBBackend.jwt;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.service.CommentService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

//form 로그인 방식을 disable을 하였기 때문에 UsernamePasswordAuthenticationFilter를 대체할 커스텀 필터 생성
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LoginFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // obtainUsername() 메서드를 오버라이드하여 email 값을 가져오도록 함
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("email");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //클라이언트 요청을 가로채서 값을 추출함
        String email = obtainUsername(request);
        String password = obtainPassword(request);

        //스프링 시큐리티에서 email 과 password를 검증하기 위해서는 token에 담아야함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //담긴 token을 AuthenticationManager로 전달하고 자동으로 검증을 진행함
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
        Integer id = customUserDetails.getId();
        String email = customUserDetails.getUsername();
        String department = customUserDetails.getDepartment();
        String nickName = customUserDetails.getNickname();

        System.out.println("successfulAuthentication id = " + id);
        System.out.println("successfulAuthentication email = " + email);

        //role 값을 가져옴
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        //토큰을 받아옴
        String token = jwtUtil.createJwt(id,email, role,nickName,department,1000 * 60 * 30L);

        //헤더 정보에 담아서 응답함(RFC 7235 인증 방식 사용)
        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.error("로그인 실패 에러 -->",failed.getMessage());
        logger.debug("Authentication failed 출력 -->",failed);

        response.setStatus(401);
    }
}
