package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    FavoriteEntity findByBoardNumberAndUserId(int boardNumber, int userId);
}
