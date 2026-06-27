package com.prepwise.prepwise_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prepwise.prepwise_backend.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>
 {
   Optional<Subject> findBySubjectName(String subjectName);

boolean existsBySubjectName(String subjectName);

    
}