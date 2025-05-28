package com.daeseonbae.DSBBackend.domain.service;

import com.daeseonbae.DSBBackend.domain.UserRole;
import com.daeseonbae.DSBBackend.domain.dto.user.JoinDTO;
import com.daeseonbae.DSBBackend.domain.entity.UserEntity;
import com.daeseonbae.DSBBackend.domain.repository.impl.UserRepositoryImpl;
import com.daeseonbae.DSBBackend.global.api.ApiException;
import com.daeseonbae.DSBBackend.global.api.AppHttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JoinService {

    private final UserRepositoryImpl userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinUser(JoinDTO joinDTO){
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        if(userRepository.existsByEmail(email)){
            throw new ApiException(AppHttpStatus.DUPLICATE_EMAIL);
        }

        UserEntity userEntity = UserEntity.builder()
                .email(joinDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(password))
                .department(joinDTO.getDepartment())
                .nickname(joinDTO.getNickName())
                .role(UserRole.USER)
                .build();

        if(!userRepository.joinUser(userEntity)){
            log.error("사용자 저장 실패 - 이메일: {}", joinDTO.getEmail());
            throw new ApiException(AppHttpStatus.USER_SAVE_FAILED);
        }

    }
}
