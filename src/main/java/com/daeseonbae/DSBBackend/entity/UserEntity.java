package com.daeseonbae.DSBBackend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"nickname"})})
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    @JsonIgnore
    private String password;

    private String department;

    private String nickname;

    private String role;
}
