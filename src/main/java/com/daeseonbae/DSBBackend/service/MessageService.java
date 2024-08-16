package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.MessageContentDTO;
import com.daeseonbae.DSBBackend.dto.MessageListDTO;
import com.daeseonbae.DSBBackend.entity.*;
import com.daeseonbae.DSBBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        BoardEntity board = boardId != null ? boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 ID입니다.")) : null;

        // 모든 메시지 리스트 가져오기
        List<MessageListEntity> messageLists = messageListRepository.findAll();

        // sender와 receiver의 위치가 바뀌더라도 동일한 대화를 찾도록 비교
        Optional<MessageListEntity> existingMessageList = messageLists.stream()
                .filter(messageList ->
                        (messageList.getSender().equals(sender) && messageList.getReceiver().equals(receiver) ||
                                messageList.getSender().equals(receiver) && messageList.getReceiver().equals(sender)) &&
                                ((board == null && messageList.getBoard() == null) ||
                                        (board != null && board.equals(messageList.getBoard())))
                )
                .findFirst();

        MessageListEntity messageList;
        if (existingMessageList.isPresent()) {
            // 기존 메시지 리스트가 있으면 사용
            messageList = existingMessageList.get();
        } else {
            // 기존 메시지 리스트가 없으면 새로 생성
            messageList = new MessageListEntity();
            messageList.setSender(sender);
            messageList.setReceiver(receiver);
            messageList.setBoard(board);
            messageList = messageListRepository.save(messageList);
        }

        // 메시지 내용 저장
        MessageContentEntity messageContent = new MessageContentEntity();
        messageContent.setMessageList(messageList);
        messageContent.setContent(content);
        messageContent.setSentDatetime(LocalDateTime.now());
        messageContentRepository.save(messageContent);

        // 최신 메시지 업데이트
        updateLatestContent(messageList);
    }

    // 최신 메시지 내용 업데이트
    public void updateLatestContent(MessageListEntity messageListEntity) {
        MessageContentEntity latestContent = messageContentRepository.findTopByMessageListOrderBySentDatetimeDesc(messageListEntity);
        if (latestContent != null) {
            messageListEntity.setLatestContent(latestContent.getContent());
            messageListRepository.save(messageListEntity);
        }
    }

    public List<MessageListDTO> getMessageList() {
        List<MessageListEntity> messageListEntities = messageListRepository.findAll();

        // 필요한 데이터만 추출해 DTO로 변환
        return messageListEntities.stream()
                .map(messageList -> new MessageListDTO(
                        messageList.getMessageId(),
                        messageList.getBoard() != null ? messageList.getBoard().getBoardNumber() : null,
                        messageList.getLatestContent()
                ))
                .collect(Collectors.toList());
    }

    // messageId로 messageContent db에서 내용 확인
    public List<MessageContentDTO> getMessageContentsByMessageId(Integer messageId) {
        MessageListEntity messageListEntity = messageListRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다."));

        List<MessageContentEntity> messageContents = messageContentRepository.findByMessageList(messageListEntity);

        return messageContents.stream()
                .map(content -> new MessageContentDTO(content.getContent(), content.getSentDatetime()))
                .collect(Collectors.toList());
    }
}

