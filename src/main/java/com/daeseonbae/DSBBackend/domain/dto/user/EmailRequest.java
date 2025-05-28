package com.daeseonbae.DSBBackend.domain.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class EmailRequest {
    private String email;
}
