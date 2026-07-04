package com.prepwise.prepwise_backend.dto.chapter;

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
public class ChapterResponse {
    private Long chapterId;
    private String chapterName;
    private String description;
    private Integer displayOrder;
    private Integer weightage;
    private Integer estimatedQuestions;
    private Long unitId;
    private String unitName;
}
