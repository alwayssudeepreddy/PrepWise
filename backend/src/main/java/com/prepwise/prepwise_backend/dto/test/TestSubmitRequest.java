package com.prepwise.prepwise_backend.dto.test;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TestSubmitRequest {
    private Map<Long, Character> answers;
}
