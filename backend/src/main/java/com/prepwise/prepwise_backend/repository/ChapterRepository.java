package com.prepwise.prepwise_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prepwise.prepwise_backend.entity.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long>
 {
   Optional<Chapter> findByChapterName(String chapterName);

boolean existsByChapterName(String chapterName);

    
}