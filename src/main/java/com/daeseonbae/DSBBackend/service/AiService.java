package com.daeseonbae.DSBBackend.service;

import com.daeseonbae.DSBBackend.dto.AiRequestDTO;
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

    public AiService(RestTemplate restTemplate, @Value("${django.server.url}") String djangoServerUrl) {
        this.restTemplate = restTemplate;
        this.djangoServerUrl = djangoServerUrl;
    }

    public String processQuery(String query) {
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

            return responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error creating JSON request", e);
        }
    }
}
