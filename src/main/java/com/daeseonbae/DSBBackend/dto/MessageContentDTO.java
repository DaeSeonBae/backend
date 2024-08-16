package com.daeseonbae.DSBBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageContentDTO {
    private String content;
    private LocalDateTime sentDatetime;
}
