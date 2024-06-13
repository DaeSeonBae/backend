package com.daeseonbae.DSBBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponseDTO {
    private Integer commentNumber;
    private String content;
    private LocalDateTime writeDatetime;
    private Integer userid;
}
