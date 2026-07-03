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
public class UnitResponse {

    private Long unitId;

    private String unitName;

    private String description;
    
    private Long subjectId;

    private String subjectName;
}