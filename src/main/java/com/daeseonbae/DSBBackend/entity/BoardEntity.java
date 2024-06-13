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
    @Column(name = "board_number")
    private Integer boardNumber;

    @Column(nullable = false)
    private String title; //제목

    @Column(nullable = false)
    private String content;  //내용

    @CreatedDate
    @Column(name = "write_datetime")
    private LocalDateTime writeDatetime; //작성 날짜

    @Column(name = "favorite_count")
    private Long favoriteCount; //좋아요 갯수

    @Column(name = "comment_count")
    private Long commentCount; //댓글 갯수

    @Column(name = "writer_email", nullable = false)
    private String writerEmail; //작성자 이메일
}
