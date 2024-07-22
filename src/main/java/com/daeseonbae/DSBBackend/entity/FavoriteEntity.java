package com.daeseonbae.DSBBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "favorite")
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "board_number")
    private Integer boardNumber;

    @Column(name = "comment_number")
    private Integer commentNumber;

    @Column(name = "user_id")
    private Integer userId;
}