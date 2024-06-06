package com.daeseonbae.DSBBackend.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardRequestDTO {
    private String title;
    private String content;
}
