package com.daeseonbae.DSBBackend.domain.entity;


import com.daeseonbae.DSBBackend.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String department;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
