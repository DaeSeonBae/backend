package com.daeseonbae.DSBBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationRequest {
    private String email;
    private String code;
}
