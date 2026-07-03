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
import com.prepwise.prepwise_backend.repository.QuestionRepository;
import com.prepwise.prepwise_backend.repository.TestRepository;
import com.prepwise.prepwise_backend.repository.TopicProgressRepository;
import com.prepwise.prepwise_backend.repository.TopicRepository;
import com.prepwise.prepwise_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TestService {

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

         System.out.println("CALLED HERE");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

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
            System.err.println("[TestService] Error querying recommendation model: " + e.getMessage());
        }

        List<Question> testQuestions;
        if (recommendedIds == null || recommendedIds.isEmpty()) {
            System.out.println("[TestService] ML recommender returned no recommendations. Falling back to fetching questions directly from DB for topic " + topicId);
            testQuestions = questionRepo.findByTopicTopicId(topicId);
            if (testQuestions.isEmpty()) {
                throw new RuntimeException("No questions found for this topic (and ML model returned no recommendations)");
            }
        } else {
            testQuestions = questionRepo.findAllById(recommendedIds);
            if (testQuestions.isEmpty()) {
                throw new RuntimeException("No questions found for recommended IDs");
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
                .orElseThrow(() -> new RuntimeException("User not found"));

        Test test = testRepo.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        if (test.getCompleted()) {
            throw new RuntimeException("Test already submitted");
        }

        // Verify the user owns the test
        if (!test.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized submission");
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
                .orElseThrow(() -> new RuntimeException("User not found"));

        Topic topic = topicRepo.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

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
