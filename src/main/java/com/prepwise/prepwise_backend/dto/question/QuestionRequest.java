package com.prepwise.prepwise_backend.dto.question;

import com.prepwise.prepwise_backend.entity.Difficulty;
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
public class QuestionRequest {
    private Long topicId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Character correctOption;
    private Difficulty difficulty;
    private String explanation;
    private Integer yearAsked;
    private Integer priority;
    private String source;
}
