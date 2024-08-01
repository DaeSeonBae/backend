package com.daeseonbae.DSBBackend.config;

import com.daeseonbae.DSBBackend.jwt.JWTFilter;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    //아래 빈의 매개변수를 위한 생성자 주입
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    //필터에 AuthenticationManager 주입을 위한 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    //비밀번호를 해시하여 저장하고 검증시켜줌
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //cors 설정
        http.
                cors((cors)-> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                //프론트 서버 주소
                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(43200L); //허용시간

                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }));


        //csrf disable
        http.csrf((auth) -> auth.disable());

        //form 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());


        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/login", "/", "/api/signup","/test","/api/email","/api/pwd/auth","/user/reset-password").permitAll()//해당 경로는 모든권한을 허용함
                .anyRequest().authenticated()); //그외의 경로에는 로그인한 사용자만 접속 가능함

        //세션 설정
        //토큰 방식이기때문에 세션이 필요 없어 STATELESS 상태로 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //의존관계 주입 및 로그인 엔드포인트 경로 변경
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/login");

        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
