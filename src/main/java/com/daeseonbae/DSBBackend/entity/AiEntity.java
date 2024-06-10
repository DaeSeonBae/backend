package com.daeseonbae.DSBBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ai")
@Data
@Getter
@Setter
public class AiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
//
    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "query", nullable = false)
    private String query;

    @Column(name = "response", nullable = false)
    private String response;
}
