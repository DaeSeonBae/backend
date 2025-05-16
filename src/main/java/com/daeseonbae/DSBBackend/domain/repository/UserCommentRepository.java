package com.daeseonbae.DSBBackend.domain.repository;

import com.daeseonbae.DSBBackend.domain.entity.CommentEntity;
import com.daeseonbae.DSBBackend.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByUser(UserEntity user);
}