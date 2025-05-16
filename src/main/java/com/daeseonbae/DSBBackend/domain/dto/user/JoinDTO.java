package com.daeseonbae.DSBBackend.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    private String email;
    private String password;
    private String department;
    private String nickName;

}
