package com.daeseonbae.DSBBackend.controller;

import com.daeseonbae.DSBBackend.dto.AiRequestDTO;
import com.daeseonbae.DSBBackend.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController {

    private final AiService apiService;

    public AiController(AiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("/api/ai")
    public ResponseEntity<String> processQuery(@RequestBody AiRequestDTO requestDTO) {
        String query = requestDTO.getQuery();
        String result = apiService.processQuery(query);
        return ResponseEntity.ok(result);
    }
}
