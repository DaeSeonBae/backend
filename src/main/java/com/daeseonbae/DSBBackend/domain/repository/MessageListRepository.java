package com.daeseonbae.DSBBackend.domain.repository;

import com.daeseonbae.DSBBackend.domain.entity.MessageListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageListRepository extends JpaRepository<MessageListEntity, Integer> {
}
