package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.AiRequestDTO;
import com.daeseonbae.DSBBackend.dto.AiResponseDTO;
import com.daeseonbae.DSBBackend.entity.AiEntity;
import com.daeseonbae.DSBBackend.repository.AiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class AiService {

    private final RestTemplate restTemplate;
    private final String djangoServerUrl;
    private final AiRepository aiRepository;

    public AiService(RestTemplate restTemplate, @Value("${django.server.url}") String djangoServerUrl, AiRepository aiRepository) {
        this.restTemplate = restTemplate;
        this.djangoServerUrl = djangoServerUrl;
        this.aiRepository = aiRepository;
    }

    public AiResponseDTO processQuery(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        AiRequestDTO requestDto = new AiRequestDTO();
        requestDto.setQuery(query);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonRequest = objectMapper.writeValueAsString(requestDto);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    djangoServerUrl + "/send_query/",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();

                AiResponseDTO responseDto = new AiResponseDTO();
                responseDto.setResponse(responseBody);

                // Save to database
                AiEntity aiEntity = new AiEntity();
                aiEntity.setQuery(query);
                aiEntity.setResponse(responseBody);
                aiEntity.setNumber(aiRepository.findAll().size() + 1);  // Set number as size of existing records + 1
                aiRepository.save(aiEntity);

                return responseDto;
            } else {
                throw new RuntimeException("Error: " + responseEntity.getStatusCode().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing query", e);
        }
    }
}
