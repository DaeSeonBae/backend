package com.daeseonbae.DSBBackend.dto.board;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class BoardInfo {
    private Integer board_number;
    private String title;
    private String content;
    private LocalDateTime write_datetime;
    private Long favorite_count;
    private Long comment_count;
    private String writer_email;

    public BoardInfo(BoardEntity boardEntity){
        this.board_number = boardEntity.getBoard_number();
        this.title = boardEntity.getTitle();
        this.content = boardEntity.getContent();
        this.write_datetime = boardEntity.getWrite_datetime();
        this.favorite_count = boardEntity.getFavorite_count();
        this.comment_count = boardEntity.getComment_count();
        this.writer_email = boardEntity.getWriter_email();
    }
}
