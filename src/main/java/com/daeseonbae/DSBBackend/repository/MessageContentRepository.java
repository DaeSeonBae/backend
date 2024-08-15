package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.MessageContentEntity;
import com.daeseonbae.DSBBackend.entity.MessageListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageContentRepository extends JpaRepository<MessageContentEntity, Integer> {
    MessageContentEntity findByMessageListAndContent(MessageListEntity messageList, String content);
}
