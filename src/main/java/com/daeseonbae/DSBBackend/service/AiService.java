package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.AiRequestDTO;
import com.daeseonbae.DSBBackend.dto.AiResponseDTO;
import com.daeseonbae.DSBBackend.entity.AiEntity;
import com.daeseonbae.DSBBackend.entity.UserEntity;
import com.daeseonbae.DSBBackend.repository.AiRepository;
import com.daeseonbae.DSBBackend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AiService {

    private final RestTemplate restTemplate;
    private final String djangoServerUrl;
    private final AiRepository aiRepository;
    private final UserRepository userRepository;

    public AiService(RestTemplate restTemplate, @Value("${django.server.url}") String djangoServerUrl, AiRepository aiRepository, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.djangoServerUrl = djangoServerUrl;
        this.aiRepository = aiRepository;
        this.userRepository = userRepository;
    }

    public AiResponseDTO processQuery(AiRequestDTO requestDTO) {
        String query = requestDTO.getQuery();
        Integer userId = requestDTO.getUserId();

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonRequest = objectMapper.writeValueAsString(requestDTO);
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
                responseDto.setQuery(query);

                // Save to database
                Optional<UserEntity> optionalUser = userRepository.findById(userId);
                if (!optionalUser.isPresent()) {
                    throw new RuntimeException("User not found");
                }

                UserEntity user = optionalUser.get();
                AiEntity aiEntity = new AiEntity();
                aiEntity.setQuery(query);
                aiEntity.setResponse(responseBody);
                aiEntity.setNumber(aiRepository.findAll().size() + 1);
                aiEntity.setUser(user);
                aiRepository.save(aiEntity);

                return responseDto;
            } else {
                throw new RuntimeException("Error: " + responseEntity.getStatusCode().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing query", e);
        }
    }

    public List<AiResponseDTO> getQueryHistory(Integer userId) {
        List<AiEntity> aiEntities = aiRepository.findByUserId(userId);
        List<AiResponseDTO> responseDtos = new ArrayList<>();

        for (AiEntity entity : aiEntities) {
            AiResponseDTO responseDto = new AiResponseDTO();
            responseDto.setQuery(entity.getQuery());
            responseDto.setResponse(entity.getResponse());
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }
}
