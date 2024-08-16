package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.MessageContentEntity;
import com.daeseonbae.DSBBackend.entity.MessageListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageContentRepository extends JpaRepository<MessageContentEntity, Integer> {
    // message_id로 메시지 내용 리스트 조회
    List<MessageContentEntity> findByMessageList_MessageId(Integer messageId);

    MessageContentEntity findTopByMessageListOrderBySentDatetimeDesc(MessageListEntity messageListEntity);
}
