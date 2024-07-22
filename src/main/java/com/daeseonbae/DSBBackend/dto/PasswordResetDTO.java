package com.daeseonbae.DSBBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDTO {
    private String email;
    private String newPassword;
}
