package com.daeseonbae.DSBBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "comment")
@Setter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_number", nullable = false)
    private Integer commentNumber;

    @ManyToOne
    @JoinColumn(name = "board_number", nullable = false)
    private BoardEntity board;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "write_datetime", nullable = false)
    private LocalDateTime writeDatetime;

    @Column(name = "favorite_count")
    private Integer favoriteCount;
}
