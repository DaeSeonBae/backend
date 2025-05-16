package com.daeseonbae.DSBBackend.domain.dto.ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiRequestDTO {
    private String query;
    private Integer userId;
}
