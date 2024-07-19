package com.daeseonbae.DSBBackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserCommentDTO {
    private Integer boardNumber;
    private String content;
    private LocalDateTime writeDatetime;
}