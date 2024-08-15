package com.daeseonbae.DSBBackend.entity;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.MessageContentEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "message_list")
public class MessageListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserEntity receiver;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    // 일대다 관계 설정
    @OneToMany(mappedBy = "messageList", cascade = CascadeType.ALL)
    private List<MessageContentEntity> messageContents = new ArrayList<>();
}
