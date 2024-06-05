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
        this.boardNumber = boardEntity.getBoard_number();
        this.title = boardEntity.getTitle();
        this.content = boardEntity.getContent();
        this.writeDatetime = boardEntity.getWrite_datetime();
        this.favoriteCount = boardEntity.getFavorite_count();
        this.commentCount = boardEntity.getComment_count();
        this.writerEmail = boardEntity.getWriter_email();
    }
}
