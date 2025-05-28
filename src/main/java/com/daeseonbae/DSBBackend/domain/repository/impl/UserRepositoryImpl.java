package com.daeseonbae.DSBBackend.domain.repository.impl;

import com.daeseonbae.DSBBackend.domain.entity.UserEntity;
import com.daeseonbae.DSBBackend.domain.repository.api.UserRepositoryCustom;
import com.daeseonbae.DSBBackend.global.api.ApiException;
import com.daeseonbae.DSBBackend.global.api.AppHttpStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserEntity findByEmail(String email) {
        return null;
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        try{
            Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);
            return count != null && count > 0;
        }catch(Exception e){
            log.error("이메일 중복 검사 실패: {}", e.getMessage(), e);
            throw new ApiException(AppHttpStatus.DATABASE_ERROR);
        }
    }

    @Override
    public boolean joinUser(UserEntity userEntity) {
        String sql = "INSERT INTO user (email, password, department, nickname, role) VALUES (?, ?, ?, ?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql,
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    userEntity.getDepartment(),
                    userEntity.getNickname(),
                    userEntity.getRole().name()
            );
            return rowsAffected > 0;
        }catch (Exception e) {
            log.error("사용자 저장 실패 - 이메일: {}, 에러: {}", userEntity.getEmail(), e.getMessage(), e);
            throw new ApiException(AppHttpStatus.DATABASE_ERROR);
        }
    }
}
