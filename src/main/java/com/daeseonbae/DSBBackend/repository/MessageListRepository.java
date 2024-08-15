package com.daeseonbae.DSBBackend.repository;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.MessageListEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageListRepository extends JpaRepository<MessageListEntity, Integer> {
    // sender, receiver, board에 따른 메시지 리스트 검색
    MessageListEntity findBySenderAndReceiverAndBoard(UserEntity sender, UserEntity receiver, BoardEntity board);
}
