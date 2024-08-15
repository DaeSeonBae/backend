package com.daeseonbae.DSBBackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "message_content")
public class MessageContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Integer contentId;

    // 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private MessageListEntity messageList;

    @Column(nullable = false)
    private String content;

    @Column(name = "sent_datetime", nullable = false)
    private LocalDateTime sentDatetime;
}
