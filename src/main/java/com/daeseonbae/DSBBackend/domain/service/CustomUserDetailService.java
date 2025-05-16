package com.daeseonbae.DSBBackend.domain.service;

import com.daeseonbae.DSBBackend.domain.dto.CustomUserDetails;
import com.daeseonbae.DSBBackend.domain.entity.UserEntity;
import com.daeseonbae.DSBBackend.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByEmail(email);

        System.out.println(userData);

        if(userData != null){
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
