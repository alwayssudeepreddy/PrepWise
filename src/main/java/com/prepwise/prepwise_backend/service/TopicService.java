package com.prepwise.prepwise_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.prepwise.prepwise_backend.exception.DuplicateResourceException;
import com.prepwise.prepwise_backend.exception.ResourceNotFoundException;

import com.prepwise.prepwise_backend.dto.topic.TopicRequest;
import com.prepwise.prepwise_backend.dto.topic.TopicResponse;
import com.prepwise.prepwise_backend.entity.Chapter;
import com.prepwise.prepwise_backend.entity.Topic;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.repository.ChapterRepository;
import com.prepwise.prepwise_backend.repository.TopicRepository;
import com.prepwise.prepwise_backend.repository.UserRepository;

@Service
@Transactional
public class TopicService {

    @Autowired
    private TopicRepository topicRepo;

    @Autowired
    private ChapterRepository chapterRepo;

    @Autowired
    private UserRepository userRepo;

    public TopicResponse addTopic(TopicRequest request) {
        if (topicRepo.existsByTopicName(request.getTopicName())) {
            throw new DuplicateResourceException("Topic already exists");
        }

        Chapter chapter = chapterRepo.findById(request.getChapterId())
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Topic topic = Topic.builder()
                .topicName(request.getTopicName())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
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
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        return mapToResponse(topic);
    }

    public TopicResponse updateTopic(Long id, TopicRequest request) {
        Topic topic = topicRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        Chapter chapter = chapterRepo.findById(request.getChapterId())
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

        topic.setTopicName(request.getTopicName());
        topic.setDescription(request.getDescription());
        topic.setDisplayOrder(request.getDisplayOrder());
        topic.setWeightage(request.getWeightage());
        topic.setChapter(chapter);

        Topic updatedTopic = topicRepo.save(topic);
        return mapToResponse(updatedTopic);
    }

    public void deleteTopic(Long id) {
        Topic topic = topicRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        topicRepo.delete(topic);
    }

    private TopicResponse mapToResponse(Topic topic) {
        return TopicResponse.builder()
                .topicId(topic.getTopicId())
                .topicName(topic.getTopicName())
                .description(topic.getDescription())
                .displayOrder(topic.getDisplayOrder())
                .weightage(topic.getWeightage())
                .chapterId(topic.getChapter().getChapterId())
                .chapterName(topic.getChapter().getChapterName())
                .build();
    }
}
