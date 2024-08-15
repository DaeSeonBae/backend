package com.daeseonbae.DSBBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageListDTO {
    private Integer messageId;
    private Integer boardId;
    private String latestContent;
}
