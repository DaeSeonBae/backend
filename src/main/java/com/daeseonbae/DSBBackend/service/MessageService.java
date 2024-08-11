package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.BoardEntity;
import com.daeseonbae.DSBBackend.entity.MessageEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.BoardRepository;
import com.daeseonbae.DSBBackend.repository.MessageRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    public void sendMessageById(Integer senderId, Integer receiverId, String content, Integer boardNumber) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 발신자 ID입니다."));
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 수신자 ID입니다."));
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 번호입니다."));

        sendMessage(sender, receiver, content, board);
    }

    public void sendMessageByEmail(Integer senderId, String receiverEmail, String content, Integer boardNumber) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 발신자 ID입니다."));
        UserEntity receiver = userRepository.findByEmail(receiverEmail);
        if (receiver == null) {
            throw new IllegalArgumentException("유효하지 않은 수신자 이메일입니다.");
        }
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 번호입니다."));

        sendMessage(sender, receiver, content, board);
    }

    private void sendMessage(UserEntity sender, UserEntity receiver, String content, BoardEntity board) {
        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentDatetime(LocalDateTime.now());
        message.setBoard(board);

        messageRepository.save(message);
    }

    public List<Object[]> getUniqueReceiverIdAndBoardNumber(Integer userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));
        List<MessageEntity> messages = messageRepository.findBySenderOrReceiver(user, user);

        // Map을 사용하여 중복 제거 및 최신 메시지 저장
        Map<String, Object[]> uniqueMap = new HashMap<>();

        for (MessageEntity message : messages) {
            String key = message.getReceiver().getId() + "-" + message.getBoard().getBoardNumber();
            if (!uniqueMap.containsKey(key) ||
                    uniqueMap.get(key)[2] == null ||
                    message.getSentDatetime().isAfter((LocalDateTime) uniqueMap.get(key)[2])) {

                uniqueMap.put(key, new Object[]{message.getReceiver().getId(), message.getBoard().getBoardNumber(), message.getSentDatetime(), message.getContent()});
            }
        }

        // Map의 값을 List로 반환
        return uniqueMap.values().stream()
                .map(array -> new Object[]{array[0], array[1], array[3]})
                .collect(Collectors.toList());
    }
}
