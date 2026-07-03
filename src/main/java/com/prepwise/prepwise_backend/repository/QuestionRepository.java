package com.prepwise.prepwise_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prepwise.prepwise_backend.entity.Question;

import java.util.Collection;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicTopicId(Long topicId);
    List<Question> findTop15ByTopicTopicIdOrderByPriorityDesc(Long topicId);
    List<Question> findByQuestionIdInOrderByPriorityDesc(Collection<Long> questionIds);
}
