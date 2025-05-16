package com.daeseonbae.DSBBackend.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimetableRequestDTO {
    private String subjectName;
    private String time;
    private String dayOfWeek;
    private String location;
}
