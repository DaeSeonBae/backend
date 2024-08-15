package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.*;
import com.daeseonbae.DSBBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageListRepository messageListRepository;

    @Autowired
    private MessageContentRepository messageContentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    public void sendMessage(Integer senderId, Integer receiverId, String receiverEmail, Integer boardId, String content) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 발신자 ID입니다."));

        UserEntity receiver;
        if (receiverId != null) {
            receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 수신자 ID입니다."));
        } else {
            receiver = userRepository.findByEmail(receiverEmail);
            if (receiver == null) {
                throw new IllegalArgumentException("유효하지 않은 수신자 이메일입니다.");
            }
        }

        // 같은 sender, receiver, board가 있는지 확인
        MessageListEntity existingMessageList = messageListRepository.findBySenderAndReceiverAndBoard(
                sender, receiver, boardId != null ? boardRepository.findById(boardId).orElse(null) : null);

        if (existingMessageList == null) {
            // 기존 메시지 리스트가 없으면 새로 생성
            MessageListEntity messageList = new MessageListEntity();
            messageList.setSender(sender);
            messageList.setReceiver(receiver);

            if (boardId != null) {
                BoardEntity board = boardRepository.findById(boardId)
                        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 ID입니다."));
                messageList.setBoard(board);
            }

            messageList = messageListRepository.save(messageList);
            existingMessageList = messageList;
        }

        // 메시지 내용 저장
        MessageContentEntity messageContent = new MessageContentEntity();
        messageContent.setMessageList(existingMessageList);
        messageContent.setContent(content);
        messageContent.setSentDatetime(LocalDateTime.now());

        messageContentRepository.save(messageContent);
    }
}

