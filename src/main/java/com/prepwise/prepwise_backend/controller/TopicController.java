package com.prepwise.prepwise_backend.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prepwise.prepwise_backend.dto.topic.TopicRequest;
import com.prepwise.prepwise_backend.dto.topic.TopicResponse;
import com.prepwise.prepwise_backend.service.TopicService;

@RestController
@RequestMapping({"/api/topics", "/api/topics/"})
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public TopicResponse addTopic(@Valid @RequestBody TopicRequest request) {
        return topicService.addTopic(request);
    }

    @GetMapping
    public List<TopicResponse> getTopics() {
        return topicService.getAllTopics();
    }

    @GetMapping("/{id}")
    public TopicResponse getTopicById(@PathVariable Long id) {
        return topicService.getTopicById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return "Deleted Topic";
    }

    @PutMapping("/{id}")
    public TopicResponse updateTopic(@PathVariable Long id, @Valid @RequestBody TopicRequest request) {
        return topicService.updateTopic(id, request);
    }
}
