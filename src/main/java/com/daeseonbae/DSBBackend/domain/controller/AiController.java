package com.daeseonbae.DSBBackend.domain.controller;

import com.daeseonbae.DSBBackend.domain.dto.ai.AiRequestDTO;
import com.daeseonbae.DSBBackend.domain.dto.ai.AiResponseDTO;
import com.daeseonbae.DSBBackend.global.jwt.JWTUtil;
import com.daeseonbae.DSBBackend.domain.service.AiService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AiController {

    private final AiService aiService;
    private final JWTUtil jwtUtil;

    public AiController(AiService aiService, JWTUtil jwtUtil) {
        this.aiService = aiService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/ai")
    public ResponseEntity<AiResponseDTO> processQuery(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
                                                      @RequestBody AiRequestDTO requestDTO) {
        // Extract token from Authorization header
        String token = authorizationHeader.substring(7);
        // Get userId from token
        Integer userId = jwtUtil.getId(token);

        // Set userId in requestDTO
        requestDTO.setUserId(userId);

        AiResponseDTO result = aiService.processQuery(requestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/ai")
    public ResponseEntity<List<AiResponseDTO>> getQueryHistory(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // Extract token from Authorization header
        String token = authorizationHeader.substring(7);
        // Get userId from token
        Integer userId = jwtUtil.getId(token);

        List<AiResponseDTO> history = aiService.getQueryHistory(userId);
        return ResponseEntity.ok(history);
    }
}
