package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.entity.MessageEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.MessageRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendMessageById(Integer senderId, Integer receiverId, String content) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 발신자 ID입니다."));
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 수신자 ID입니다."));

        sendMessage(sender, receiver, content);
    }

    public void sendMessageByEmail(Integer senderId, String receiverEmail, String content) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 발신자 ID입니다."));
        UserEntity receiver = userRepository.findByEmail(receiverEmail);
        if (receiver == null) {
            throw new IllegalArgumentException("유효하지 않은 수신자 이메일입니다.");
        }

        sendMessage(sender, receiver, content);
    }

    private void sendMessage(UserEntity sender, UserEntity receiver, String content) {
        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentDatetime(LocalDateTime.now());

        messageRepository.save(message);
    }
}
