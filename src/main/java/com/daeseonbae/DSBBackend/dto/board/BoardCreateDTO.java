package com.daeseonbae.DSBBackend.dto.board;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardCreateDTO {
    private String title;
    private String content;
    private String writer_email;
}
