package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.MessageContentEntity;
import com.daeseonbae.DSBBackend.entity.MessageListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageContentRepository extends JpaRepository<MessageContentEntity, Integer> {
    MessageContentEntity findTopByMessageListOrderBySentDatetimeDesc(MessageListEntity messageListEntity);

    List<MessageContentEntity> findByMessageList(MessageListEntity messageListEntity);

    // 특정 message_list와 연관된 메시지를 모두 삭제
    void deleteByMessageList(MessageListEntity messageList);
}
