package com.prepwise.prepwise_backend.dto.subject;

import jakarta.validation.constraints.NotBlank;
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
public class SubjectRequest {
    @NotBlank(message = "Subject name is required")
    private String subjectName;
    
    private String description;
    
    
    
}