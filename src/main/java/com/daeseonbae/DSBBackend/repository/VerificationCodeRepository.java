package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.VerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationEntity, Long> {
    Optional<VerificationEntity> findByEmailAndCode(String email, String code);
    void deleteByEmailAndCode(String email, String code);
    void deleteByCreatedAtBefore(LocalDateTime expiryTime);
}
