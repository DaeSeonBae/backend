package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.MessageListDTO;
import com.daeseonbae.DSBBackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam(required = false) Integer receiverId,
            @RequestParam(required = false) String receiverEmail,
            @RequestParam Integer boardId,
            @RequestParam String content,
            Authentication authentication) {

        // 수신자 ID와 이메일이 모두 없을 경우, 잘못된 요청으로 응답
        if (receiverId == null && receiverEmail == null) {
            return ResponseEntity.badRequest().body("Receiver ID 또는 Email을 제공해야 합니다.");
        }

        // 인증된 사용자로부터 발신자 ID를 추출
        Integer senderId = ((com.daeseonbae.DSBBackend.dto.CustomUserDetails) authentication.getPrincipal()).getId();
        messageService.sendMessage(senderId, receiverId, receiverEmail, boardId, content);

        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<MessageListDTO>> getMessageList() {
        List<MessageListDTO> response = messageService.getMessageList();
        return ResponseEntity.ok(response);
    }
}
