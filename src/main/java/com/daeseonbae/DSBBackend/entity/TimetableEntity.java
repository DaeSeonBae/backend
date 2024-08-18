package com.daeseonbae.DSBBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "timetable")
@Getter
@Setter
@NoArgsConstructor
public class TimetableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private String location;
}