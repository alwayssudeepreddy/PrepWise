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

import com.prepwise.prepwise_backend.dto.question.QuestionRequest;
import com.prepwise.prepwise_backend.dto.question.QuestionResponse;
import com.prepwise.prepwise_backend.service.QuestionService;

@RestController
@RequestMapping({"/api/questions", "/api/questions/"})
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public QuestionResponse addQuestion(@Valid @RequestBody QuestionRequest request) {
        return questionService.addQuestion(request);
    }

    @GetMapping
    public List<QuestionResponse> getQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public QuestionResponse getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @GetMapping("/topic/{topicId}")
    public List<QuestionResponse> getQuestionsByTopicId(@PathVariable Long topicId) {
        return questionService.getQuestionsByTopicId(topicId);
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return "Deleted Question";
    }

    @PutMapping("/{id}")
    public QuestionResponse updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        return questionService.updateQuestion(id, request);
    }
}
