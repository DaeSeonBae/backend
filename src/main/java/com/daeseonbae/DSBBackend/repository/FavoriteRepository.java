package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    FavoriteEntity findByBoardNumberAndUserId(int boardNumber, int userId);
    FavoriteEntity findByCommentNumberAndUserId(int commentNumber, int userId);
    List<FavoriteEntity> findByUserId(int userId);
}
