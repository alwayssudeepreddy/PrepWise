package com.prepwise.prepwise_backend.controller;

import com.prepwise.prepwise_backend.dto.test.TestGenerateRequest;
import com.prepwise.prepwise_backend.dto.test.TestResponse;
import com.prepwise.prepwise_backend.dto.test.TestSubmitRequest;
import com.prepwise.prepwise_backend.dto.test.TopicProgressResponse;
import com.prepwise.prepwise_backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/tests", "/api/tests/"})
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/generate")
    public TestResponse generateTest(@RequestBody TestGenerateRequest request) {
        return testService.generateTest(request.getTopicId());
    }

    @PostMapping("/{testId}/submit")
    public TestResponse submitTest(@PathVariable Long testId, @RequestBody TestSubmitRequest request) {
        return testService.submitTest(testId, request);
    }

    @GetMapping("/progress/topic/{topicId}")
    public TopicProgressResponse getTopicProgress(@PathVariable Long topicId) {
        return testService.getTopicProgress(topicId);
    }
}
