package com.prepwise.prepwise_backend.dto.unit;


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
public class UnitRequest {
    private String unitName;

    private String description;

    private Integer estimatedQuestions;

    private Long subjectId;
}
