package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Boolean existsByEmail(String email);

    UserEntity findByEmail(String email);
}
