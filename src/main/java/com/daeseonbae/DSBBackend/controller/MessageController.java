package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam(required = false) Integer receiverId,
            @RequestParam(required = false) String receiverEmail,
            @RequestParam String content,
            Authentication authentication) {

        if (receiverId == null && receiverEmail == null) {
            return ResponseEntity.badRequest().body("Receiver ID 또는 Email을 제공해야 합니다.");
        }

        Integer senderId = ((com.daeseonbae.DSBBackend.dto.CustomUserDetails) authentication.getPrincipal()).getId();
        if (receiverId != null) {
            messageService.sendMessageById(senderId, receiverId, content);
        } else {
            messageService.sendMessageByEmail(senderId, receiverEmail, content);
        }

        return ResponseEntity.ok("메시지가 성공적으로 전송되었습니다.");
    }
}
