package com.daeseonbae.DSBBackend.domain.repository;

import com.daeseonbae.DSBBackend.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByBoardBoardNumber(Integer boardNumber);
}
