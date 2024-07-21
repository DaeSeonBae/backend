package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Integer> {
    List<BoardEntity> findByWriterEmail(String email);

    @Query("SELECT b FROM BoardEntity b WHERE b.favoriteCount >= 10 AND b.writeDatetime >= :sevenDaysAgo")
    List<BoardEntity> findRecentHotBoards(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
