package com.prepwise.prepwise_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.prepwise.prepwise_backend.dto.topic.TopicRequest;
import com.prepwise.prepwise_backend.dto.topic.TopicResponse;
import com.prepwise.prepwise_backend.entity.Chapter;
import com.prepwise.prepwise_backend.entity.Topic;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.repository.ChapterRepository;
import com.prepwise.prepwise_backend.repository.TopicRepository;
import com.prepwise.prepwise_backend.repository.UserRepository;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepo;

    @Autowired
    private ChapterRepository chapterRepo;

    @Autowired
    private UserRepository userRepo;

    public TopicResponse addTopic(TopicRequest request) {
        if (topicRepo.existsByTopicName(request.getTopic_name())) {
            throw new RuntimeException("Topic already exists");
        }

        Chapter chapter = chapterRepo.findById(request.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Topic topic = Topic.builder()
                .topicName(request.getTopic_name())
                .desciption(request.getDesciption())
                .displayorder(request.getDisplayorder())
                .weightage(request.getWeightage())
                .chapter(chapter)
                .user(user)
                .build();

        Topic savedTopic = topicRepo.save(topic);
        return mapToResponse(savedTopic);
    }

    public List<TopicResponse> getAllTopics() {
        return topicRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TopicResponse getTopicById(Long id) {
        Topic topic = topicRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        return mapToResponse(topic);
    }

    public TopicResponse updateTopic(Long id, TopicRequest request) {
        Topic topic = topicRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        Chapter chapter = chapterRepo.findById(request.getChapterId())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        topic.setTopicName(request.getTopic_name());
        topic.setDesciption(request.getDesciption());
        topic.setDisplayorder(request.getDisplayorder());
        topic.setWeightage(request.getWeightage());
        topic.setChapter(chapter);

        Topic updatedTopic = topicRepo.save(topic);
        return mapToResponse(updatedTopic);
    }

    public void deleteTopic(Long id) {
        Topic topic = topicRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        topicRepo.delete(topic);
    }

    private TopicResponse mapToResponse(Topic topic) {
        return TopicResponse.builder()
                .topicId(topic.getTopicId())
                .topic_name(topic.getTopicName())
                .desciption(topic.getDesciption())
                .displayorder(topic.getDisplayorder())
                .weightage(topic.getWeightage())
                .chapterId(topic.getChapter().getChapterId())
                .chapterName(topic.getChapter().getChapterName())
                .build();
    }
}
