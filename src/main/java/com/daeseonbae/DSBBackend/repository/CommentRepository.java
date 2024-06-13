package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    // 특정 게시물에 대한 가장 높은 commentNumber를 찾는 쿼리
    @Query(value = "SELECT COALESCE(MAX(c.commentNumber), 0) FROM CommentEntity c WHERE c.board.board_number = :boardId")
    Integer findMaxCommentNumberByBoardId(@Param("boardId") Integer boardId);
}
