package com.daeseonbae.DSBBackend.domain.dto.userinfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBoardDTO {
    private String title;
    private Long favoriteCount;
    private Long commentCount;
}
