package com.prepwise.prepwise_backend.dto.subject;

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
public class SubjectResponse {

    private Long subjectId;
    private String subjectName;
    private String description;
}