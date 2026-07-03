package com.prepwise.prepwise_backend.dto.test;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TopicProgressResponse {
    private Long topicId;
    private String topicName;
    private Map<Long, Boolean> questionStatus;
}
