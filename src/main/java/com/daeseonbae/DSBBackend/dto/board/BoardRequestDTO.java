package com.daeseonbae.DSBBackend.dto.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class BoardRequestDTO {
    private String title;
    private String content;

    private MultipartFile image;
}
