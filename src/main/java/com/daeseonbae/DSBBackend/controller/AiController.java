package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.AiRequestDTO;
import com.daeseonbae.DSBBackend.dto.AiResponseDTO;
import com.daeseonbae.DSBBackend.service.AiService;
import com.daeseonbae.DSBBackend.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
}
