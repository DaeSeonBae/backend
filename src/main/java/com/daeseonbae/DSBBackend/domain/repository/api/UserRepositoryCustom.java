package com.daeseonbae.DSBBackend.domain.repository.api;

import com.daeseonbae.DSBBackend.domain.entity.UserEntity;

public interface UserRepositoryCustom {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
    boolean joinUser(UserEntity userEntity);
    
}
