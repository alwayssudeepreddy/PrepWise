package com.prepwise.prepwise_backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prepwise.prepwise_backend.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTopicName(String topicName);
    boolean existsByTopicName(String topicName);
}
