package com.prepwise.prepwise_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.prepwise.prepwise_backend.exception.ResourceNotFoundException;

import com.prepwise.prepwise_backend.dto.question.QuestionRequest;
import com.prepwise.prepwise_backend.dto.question.QuestionResponse;
import com.prepwise.prepwise_backend.entity.Question;
import com.prepwise.prepwise_backend.entity.Topic;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.repository.QuestionRepository;
import com.prepwise.prepwise_backend.repository.TopicRepository;
import com.prepwise.prepwise_backend.repository.UserRepository;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private TopicRepository topicRepo;

    @Autowired
    private UserRepository userRepo;

    public QuestionResponse addQuestion(QuestionRequest request) {
        Topic topic = topicRepo.findById(request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Question question = Question.builder()
                .topic(topic)
                .user(user)
                .questionText(request.getQuestionText())
                .optionA(request.getOptionA())
                .optionB(request.getOptionB())
                .optionC(request.getOptionC())
                .optionD(request.getOptionD())
                .correctOption(request.getCorrectOption())
                .difficulty(request.getDifficulty())
                .explanation(request.getExplanation())
                .yearAsked(request.getYearAsked())
                .priority(request.getPriority())
                .source(request.getSource())
                .build();

        Question savedQuestion = questionRepo.save(question);
        return mapToResponse(savedQuestion);
    }

    public List<QuestionResponse> getAllQuestions() {
        return questionRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public QuestionResponse getQuestionById(Long id) {
        Question question = questionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        return mapToResponse(question);
    }

    public List<QuestionResponse> getQuestionsByTopicId(Long topicId) {
        return questionRepo.findByTopicTopicId(topicId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question question = questionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Topic topic = topicRepo.findById(request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        question.setTopic(topic);
        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectOption(request.getCorrectOption());
        question.setDifficulty(request.getDifficulty());
        question.setExplanation(request.getExplanation());
        question.setYearAsked(request.getYearAsked());
        question.setPriority(request.getPriority());
        question.setSource(request.getSource());

        Question updatedQuestion = questionRepo.save(question);
        return mapToResponse(updatedQuestion);
    }

    public void deleteQuestion(Long id) {
        Question question = questionRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        questionRepo.delete(question);
    }

    private QuestionResponse mapToResponse(Question question) {
        return QuestionResponse.builder()
                .questionId(question.getQuestionId())
                .topicId(question.getTopic().getTopicId())
                .topicName(question.getTopic().getTopicName())
                .questionText(question.getQuestionText())
                .optionA(question.getOptionA())
                .optionB(question.getOptionB())
                .optionC(question.getOptionC())
                .optionD(question.getOptionD())
                .correctOption(question.getCorrectOption())
                .difficulty(question.getDifficulty())
                .explanation(question.getExplanation())
                .yearAsked(question.getYearAsked())
                .priority(question.getPriority())
                .source(question.getSource())
                .build();
    }
}
