package com.daeseonbae.DSBBackend.dto.board;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardResponseDTO {
    private Integer boardNumber;
    private String title;
    private String content;
    private LocalDateTime writeDatetime;
    private Long favoriteCount;
    private Long commentCount;
    private String writerEmail;

    public BoardResponseDTO (BoardEntity boardEntity){
        this.boardNumber = boardEntity.getBoardNumber();
        this.title = boardEntity.getTitle();
        this.content = boardEntity.getContent();
        this.writeDatetime = boardEntity.getWriteDatetime();
        this.favoriteCount = boardEntity.getFavoriteCount();
        this.commentCount = boardEntity.getCommentCount();
        this.writerEmail = boardEntity.getWriterEmail();
    }
}
