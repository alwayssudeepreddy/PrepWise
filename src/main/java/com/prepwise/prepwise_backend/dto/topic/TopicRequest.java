package com.prepwise.prepwise_backend.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicRequest {
    private String topicName;
    private String description;
    private Integer displayOrder;
    private Integer weightage;
    private Long chapterId;
}
