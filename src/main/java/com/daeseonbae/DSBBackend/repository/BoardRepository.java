package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> findByWriterEmail(String email);
}
