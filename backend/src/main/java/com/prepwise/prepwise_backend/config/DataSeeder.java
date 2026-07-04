package com.prepwise.prepwise_backend.config;

import com.prepwise.prepwise_backend.entity.*;
import com.prepwise.prepwise_backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("No users found. Seeding default database data...");

            // 1. Seed Users
            User admin = User.builder()
                    .username("admin")
                    .fullName("System Administrator")
                    .email("admin@prepwise.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            admin = userRepository.save(admin);

            User student = User.builder()
                    .username("student")
                    .fullName("John Doe")
                    .email("student@prepwise.com")
                    .password(passwordEncoder.encode("student123"))
                    .role(Role.STUDENT)
                    .build();
            student = userRepository.save(student);

            log.info("Seeded default users: admin/admin123 and student/student123");

            // 2. Seed Subject
            Subject subject = Subject.builder()
                    .subjectName("Mathematics")
                    .description("Advanced Mathematics and Calculus")
                    .build();
            subject = subjectRepository.save(subject);

            // 3. Seed Unit
            Unit unit = Unit.builder()
                    .unitName("Calculus")
                    .description("Differential and Integral Calculus")
                    .subject(subject)
                    .user(admin)
                    .build();
            unit = unitRepository.save(unit);

            // 4. Seed Chapter
            Chapter chapter = Chapter.builder()
                    .chapterName("Limits and Continuity")
                    .description("Core concepts of limits, continuity, and differentiability")
                    .displayOrder(1)
                    .weightage(10)
                    .estimatedQuestions(15)
                    .unit(unit)
                    .user(admin)
                    .build();
            chapter = chapterRepository.save(chapter);

            // 5. Seed Topic
            Topic topic = Topic.builder()
                    .topicName("Limits of Functions")
                    .description("Evaluation of limits, standard limit theorems, and indeterminate forms")
                    .displayOrder(1)
                    .weightage(5)
                    .chapter(chapter)
                    .user(admin)
                    .build();
            topic = topicRepository.save(topic);

            log.info("Seeded subject, unit, chapter, and topic. Topic ID: {}", topic.getTopicId());

            // 6. Seed Questions
            List<Question> questions = new ArrayList<>();
            
            questions.add(Question.builder()
                    .topic(topic)
                    .user(admin)
                    .questionText("What is the limit of (sin x)/x as x approaches 0?")
                    .optionA("0")
                    .optionB("1")
                    .optionC("Infinity")
                    .optionD("Undefined")
                    .correctOption('B')
                    .difficulty(Difficulty.EASY)
                    .explanation("Using the standard trigonometric limit theorem, limit of sin(x)/x as x -> 0 is 1.")
                    .yearAsked(2021)
                    .priority(1)
                    .source("Standard Calculus")
                    .build());

            questions.add(Question.builder()
                    .topic(topic)
                    .user(admin)
                    .questionText("What is the derivative of x^2 with respect to x?")
                    .optionA("x")
                    .optionB("2")
                    .optionC("2x")
                    .optionD("x^2/2")
                    .correctOption('C')
                    .difficulty(Difficulty.EASY)
                    .explanation("Using the power rule d/dx(x^n) = n*x^(n-1), d/dx(x^2) = 2x.")
                    .yearAsked(2020)
                    .priority(2)
                    .source("Algebra & Calculus")
                    .build());

            questions.add(Question.builder()
                    .topic(topic)
                    .user(admin)
                    .questionText("What is the limit of (1 + 1/n)^n as n approaches infinity?")
                    .optionA("0")
                    .optionB("1")
                    .optionC("e")
                    .optionD("Infinity")
                    .correctOption('C')
                    .difficulty(Difficulty.MEDIUM)
                    .explanation("The mathematical constant e is defined as the limit of (1 + 1/n)^n as n approaches infinity.")
                    .yearAsked(2022)
                    .priority(3)
                    .source("Analysis")
                    .build());

            questions.add(Question.builder()
                    .topic(topic)
                    .user(admin)
                    .questionText("What is the limit of (x^2 - 1)/(x - 1) as x approaches 1?")
                    .optionA("0")
                    .optionB("1")
                    .optionC("2")
                    .optionD("Undefined")
                    .correctOption('C')
                    .difficulty(Difficulty.MEDIUM)
                    .explanation("Factor the numerator: (x-1)(x+1)/(x-1) = x+1. As x approaches 1, x+1 approaches 2.")
                    .yearAsked(2019)
                    .priority(4)
                    .source("AP Calculus")
                    .build());

            questions.add(Question.builder()
                    .topic(topic)
                    .user(admin)
                    .questionText("Is the function f(x) = |x| differentiable at x = 0?")
                    .optionA("Yes, and derivative is 0")
                    .optionB("No, because the left-hand and right-hand derivatives do not match")
                    .optionC("Yes, and derivative is 1")
                    .optionD("Only differentiable from the left")
                    .correctOption('B')
                    .difficulty(Difficulty.HARD)
                    .explanation("The left-hand derivative is -1 and the right-hand derivative is +1. Since they are unequal, it is not differentiable at x=0.")
                    .yearAsked(2023)
                    .priority(5)
                    .source("Advanced Calculus")
                    .build());

            questionRepository.saveAll(questions);
            log.info("Seeded {} default questions for topic 'Limits of Functions'.", questions.size());
        } else {
            log.info("Database already populated. Skipping seeder.");
        }
    }
}
