package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@RestController
public class UserInfoController {

    @GetMapping("/api/user-info")
    public UserEntity userInfo(Authentication authentication) {
        // 사용자 정보 가져오기
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String nickName = customUserDetails.getNickname();
        String email = customUserDetails.getUsername();
        String department = customUserDetails.getDepartment();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        UserEntity userEntity = new UserEntity();

        userEntity.setNickname(nickName);
        userEntity.setEmail(email);
        userEntity.setDepartment(department);
        userEntity.setRole(role);

        return userEntity;
    }

}
