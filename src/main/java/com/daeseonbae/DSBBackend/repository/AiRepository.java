package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.AiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository extends JpaRepository<AiEntity, Integer> {
}
