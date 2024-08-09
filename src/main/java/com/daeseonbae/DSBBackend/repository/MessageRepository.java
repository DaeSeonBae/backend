package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.MessageEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    // 특정 사용자가 발신자 또는 수신자인 메시지들을 가져옴
    List<MessageEntity> findBySenderOrReceiver(UserEntity sender, UserEntity receiver);
}
