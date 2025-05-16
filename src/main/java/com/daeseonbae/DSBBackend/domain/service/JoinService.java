package com.daeseonbae.DSBBackend.domain.service;

import com.daeseonbae.DSBBackend.domain.dto.user.JoinDTO;
import com.daeseonbae.DSBBackend.domain.entity.UserEntity;
import com.daeseonbae.DSBBackend.domain.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean joinProcess(JoinDTO joinDTO){
        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByEmail(email); //이메일 중복성 검사

        if(isExist){
            return false;
        }

        UserEntity data = new UserEntity();

        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setDepartment(joinDTO.getDepartment());
        data.setNickname(joinDTO.getNickName());
        data.setRole("USER");

        userRepository.save(data);

        return true;
    }
}
