package com.prepwise.prepwise_backend.service;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelClientService {

    @Value("${model.api.url:http://localhost:5000/predict}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Long> getRecommendedQuestions(Long currentTopicId, List<Long> solvedQuestions, List<Long> wrongQuestions) {
        try {

           
            ModelRequest requestPayload = ModelRequest.builder()
                    .currentTopicId(currentTopicId)
                    .solvedQuestions(solvedQuestions)
                    .wrongQuestions(wrongQuestions)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ModelRequest> entity = new HttpEntity<>(requestPayload, headers);

            ModelResponse response = restTemplate.postForObject(apiUrl, entity, ModelResponse.class);

            if (response != null && response.getQuestionIds() != null) {
                return response.getQuestionIds();
            }
        } catch (Exception e) {
            System.err.println("[ModelClientService] Failed to query recommendation model, falling back to local heuristic. Error: " + e.getMessage());
        }
        return null;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelRequest {
        private Long currentTopicId;
        private List<Long> solvedQuestions;
        private List<Long> wrongQuestions;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModelResponse {
        private List<Long> questionIds;
    }
}
