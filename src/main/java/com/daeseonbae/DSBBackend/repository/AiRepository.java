package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.AiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiRepository extends JpaRepository<AiEntity, Integer> {
    List<AiEntity> findByUserId(Integer userId);
}
