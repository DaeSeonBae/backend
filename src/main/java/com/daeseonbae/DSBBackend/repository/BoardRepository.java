package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Integer> {

}
