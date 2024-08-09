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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    //수신자의 ID로 메시지를 전송
    public void sendMessageById(Integer senderId, Integer receiverId, String content, Integer boardNumber) {
        // 발신자 정보 조회
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 발신자 ID입니다."));

        // 수신자 정보 조회
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 수신자 ID입니다."));

        // 게시글 정보 조회
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 번호입니다."));

        sendMessage(sender, receiver, content, board);
    }

    //수신자의 이메일로 메시지를 전송
    public void sendMessageByEmail(Integer senderId, String receiverEmail, String content, Integer boardNumber) {
        // 발신자 정보 조회
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 발신자 ID입니다."));

        // 이메일로 수신자 정보 조회
        UserEntity receiver = userRepository.findByEmail(receiverEmail);
        if (receiver == null) {
            throw new IllegalArgumentException("유효하지 않은 수신자 이메일입니다.");
        }

        // 게시글 정보 조회
        BoardEntity board = boardRepository.findById(boardNumber)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 번호입니다."));

        sendMessage(sender, receiver, content, board);
    }

    //쪽지 db 저장
    private void sendMessage(UserEntity sender, UserEntity receiver, String content, BoardEntity board) {
        MessageEntity message = new MessageEntity();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentDatetime(LocalDateTime.now());
        message.setBoard(board);

        messageRepository.save(message);
    }

    //사용자가 보낸 메시지에서 고유한 수신자 ID와 보드 번호 조합을 반환
    public List<Object[]> getUniqueReceiverIdAndBoardNumber(Integer userId) {
        // 사용자 정보 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        // 사용자가 발신자 또는 수신자인 메시지 리스트 조회
        List<MessageEntity> messages = messageRepository.findBySenderOrReceiver(user, user);

        // Set을 사용하여 중복 제거
        Set<String> uniqueSet = new HashSet<>();
        return messages.stream()
                .map(m -> new Object[]{m.getReceiver().getId(), m.getBoard().getBoardNumber()})
                .filter(array -> uniqueSet.add(array[0] + "-" + array[1])) // 수신자 ID와 보드 번호 조합의 중복 제거
                .collect(Collectors.toList());
    }
}
