package com.daeseonbae.DSBBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer board_number;

    @Column(nullable = false)
    private String title; //제목

    @Column(nullable = false)
    private String content;  //내용

    @CreatedDate
    private LocalDateTime write_datetime; //작성 날짜

    private Long favorite_count; //좋아요 갯수

    private Long comment_count; //댓글 갯수

    @Column(nullable = false)
    private String writer_email; //작성자 이메일

}
