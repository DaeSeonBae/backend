package com.daeseonbae.DSBBackend.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDTO {
    private String email;
    private String newPassword;
}
