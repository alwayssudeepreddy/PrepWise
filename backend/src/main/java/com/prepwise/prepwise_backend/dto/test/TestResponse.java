package com.prepwise.prepwise_backend.dto.test;

import com.prepwise.prepwise_backend.dto.question.QuestionResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestResponse {
    private Long testId;
    private Long topicId;
    private String topicName;
    private List<QuestionResponse> questions;
    private Integer score;
    private Boolean completed;
    private LocalDateTime createdAt;
}
