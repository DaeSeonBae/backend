package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {
}
