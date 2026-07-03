package com.prepwise.prepwise_backend.repository;

import com.prepwise.prepwise_backend.entity.TopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicProgressRepository extends JpaRepository<TopicProgress, Long> {
    Optional<TopicProgress> findByUserUserIdAndTopicTopicId(Long userId, Long topicId);
    List<TopicProgress> findByUserUserId(Long userId);
}
