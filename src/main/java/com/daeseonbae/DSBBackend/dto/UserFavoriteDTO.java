package com.daeseonbae.DSBBackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserFavoriteDTO {
    private Long id;
    private int boardNumber;
}
