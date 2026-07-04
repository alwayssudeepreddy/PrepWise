package com.prepwise.prepwise_backend.service;

import com.prepwise.prepwise_backend.dto.question.QuestionResponse;
import com.prepwise.prepwise_backend.dto.test.TestResponse;
import com.prepwise.prepwise_backend.dto.test.TestSubmitRequest;
import com.prepwise.prepwise_backend.dto.test.TopicProgressResponse;
import com.prepwise.prepwise_backend.entity.Question;
import com.prepwise.prepwise_backend.entity.Test;
import com.prepwise.prepwise_backend.entity.Topic;
import com.prepwise.prepwise_backend.entity.TopicProgress;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.exception.BadRequestException;
import com.prepwise.prepwise_backend.exception.ForbiddenActionException;
import com.prepwise.prepwise_backend.exception.ResourceNotFoundException;
import com.prepwise.prepwise_backend.repository.QuestionRepository;
import com.prepwise.prepwise_backend.repository.TestRepository;
import com.prepwise.prepwise_backend.repository.TopicProgressRepository;
import com.prepwise.prepwise_backend.repository.TopicRepository;
import com.prepwise.prepwise_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private TestRepository testRepo;

    @Autowired
    private TopicRepository topicRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private TopicProgressRepository topicProgressRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelClientService modelClientService;

    public TestResponse generateTest(Long topicId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        // Get user's progress across previous topics
        List<TopicProgress> allProgress = topicProgressRepo.findByUserUserId(user.getUserId());
        List<Long> solvedQuestions = new ArrayList<>();
        List<Long> wrongQuestions = new ArrayList<>();

        for (TopicProgress progress : allProgress) {
            if (!progress.getTopic().getTopicId().equals(topicId)) {
                Map<Long, Boolean> statusMap = progress.getQuestionStatus();
                if (statusMap != null) {
                    for (Map.Entry<Long, Boolean> entry : statusMap.entrySet()) {
                        if (Boolean.TRUE.equals(entry.getValue())) {
                            solvedQuestions.add(entry.getKey());
                        } else {
                            wrongQuestions.add(entry.getKey());
                        }
                    }
                }
            }
        }

        // Call ML recommender service
        List<Long> recommendedIds = null;
        try {
            recommendedIds = modelClientService.getRecommendedQuestions(topicId, solvedQuestions, wrongQuestions);
        } catch (Exception e) {
            log.error("Error querying recommendation model", e);
        }

        List<Question> testQuestions;
        if (recommendedIds == null || recommendedIds.isEmpty()) {
            log.info("ML recommender returned no recommendations; falling back to question bank for topic {}", topicId);
            testQuestions = questionRepo.findByTopicTopicId(topicId);
            if (testQuestions.isEmpty()) {
                throw new ResourceNotFoundException("No questions found for this topic (and ML model returned no recommendations)");
            }
        } else {
            testQuestions = questionRepo.findAllById(recommendedIds);
            if (testQuestions.isEmpty()) {
                throw new ResourceNotFoundException("No questions found for recommended IDs");
            }
        }

        // Create and save Test
        Test test = Test.builder()
                .user(user)
                .topic(topic)
                .questions(testQuestions)
                .score(0)
                .completed(false)
                .build();

        Test savedTest = testRepo.save(test);
        return mapToResponse(savedTest);
    }

    public TestResponse submitTest(Long testId, TestSubmitRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Test test = testRepo.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        if (test.getCompleted()) {
            throw new BadRequestException("Test already submitted");
        }

        // Verify the user owns the test
        if (!test.getUser().getUserId().equals(user.getUserId())) {
            throw new ForbiddenActionException("You are not allowed to submit this test");
        }

        TopicProgress progress = topicProgressRepo.findByUserUserIdAndTopicTopicId(user.getUserId(), test.getTopic().getTopicId())
                .orElseGet(() -> TopicProgress.builder()
                        .user(user)
                        .topic(test.getTopic())
                        .questionStatus(new HashMap<>())
                        .build());

        int score = 0;
        Map<Long, Character> submittedAnswers = request.getAnswers() != null ? request.getAnswers() : new HashMap<>();

        for (Question question : test.getQuestions()) {
            Long qId = question.getQuestionId();
            Character submittedOption = submittedAnswers.get(qId);
            
            if (submittedOption != null) {
                // Ignore case comparison
                boolean isCorrect = Character.toUpperCase(submittedOption) == Character.toUpperCase(question.getCorrectOption());
                progress.getQuestionStatus().put(qId, isCorrect);
                if (isCorrect) {
                    score++;
                }
            } else {
                // If they didn't answer, record as incorrect
                progress.getQuestionStatus().put(qId, false);
            }
        }

        // Save progress and update test
        topicProgressRepo.save(progress);

        test.setScore(score);
        test.setCompleted(true);
        Test updatedTest = testRepo.save(test);

        return mapToResponse(updatedTest);
    }

    public TopicProgressResponse getTopicProgress(Long topicId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        TopicProgress progress = topicProgressRepo.findByUserUserIdAndTopicTopicId(user.getUserId(), topicId)
                .orElse(null);

        Map<Long, Boolean> statusMap = (progress != null && progress.getQuestionStatus() != null) 
                ? progress.getQuestionStatus() 
                : new HashMap<>();

        return TopicProgressResponse.builder()
                .topicId(topicId)
                .topicName(topic.getTopicName())
                .questionStatus(statusMap)
                .build();
    }

    private TestResponse mapToResponse(Test test) {
        List<QuestionResponse> questionResponses = test.getQuestions().stream()
                .map(this::mapQuestionToResponse)
                .toList();

        return TestResponse.builder()
                .testId(test.getTestId())
                .topicId(test.getTopic().getTopicId())
                .topicName(test.getTopic().getTopicName())
                .questions(questionResponses)
                .score(test.getScore())
                .completed(test.getCompleted())
                .createdAt(test.getCreatedAt())
                .build();
    }

    private QuestionResponse mapQuestionToResponse(Question question) {
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
