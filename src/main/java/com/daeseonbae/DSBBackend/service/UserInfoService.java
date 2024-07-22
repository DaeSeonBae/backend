package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.dto.PasswordResetDTO;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Service
public class UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity getUserInfo(Authentication authentication) {
        // 사용자 정보 가져오기
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Integer id = customUserDetails.getId();
        String email = customUserDetails.getUsername();
        String department = customUserDetails.getDepartment();
        String nickName = customUserDetails.getNickname();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        UserEntity userEntity = new UserEntity();

        userEntity.setId(id);
        userEntity.setEmail(email);
        userEntity.setDepartment(department);
        userEntity.setNickname(nickName);
        userEntity.setRole(role);

        return userEntity;
    }

    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        UserEntity user = userRepository.findByEmail(passwordResetDTO.getEmail());
        if (user != null) {
            user.setPassword(bCryptPasswordEncoder.encode(passwordResetDTO.getNewPassword()));
            userRepository.save(user);
        }
    }
}
