package com.prepwise.prepwise_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prepwise.prepwise_backend.entity.Question;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicTopicId(Long topicId);
}
