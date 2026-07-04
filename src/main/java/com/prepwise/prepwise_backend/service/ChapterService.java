package com.prepwise.prepwise_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.prepwise.prepwise_backend.exception.DuplicateResourceException;
import com.prepwise.prepwise_backend.exception.ResourceNotFoundException;

import com.prepwise.prepwise_backend.dto.chapter.ChapterRequest;
import com.prepwise.prepwise_backend.dto.chapter.ChapterResponse;
import com.prepwise.prepwise_backend.entity.Chapter;
import com.prepwise.prepwise_backend.entity.Unit;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.repository.ChapterRepository;
import com.prepwise.prepwise_backend.repository.UnitRepository;
import com.prepwise.prepwise_backend.repository.UserRepository;

@Service
@Transactional
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepo;

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private UserRepository userRepo;

    public ChapterResponse addChapter(ChapterRequest request) {

        if (chapterRepo.existsByChapterName(request.getChapterName())) {
            throw new DuplicateResourceException("Chapter already exists");
        }

        Unit unit = unitRepo.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Chapter chapter = Chapter.builder()
                .chapterName(request.getChapterName())
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder())
                .weightage(request.getWeightage())
                .estimatedQuestions(request.getEstimatedQuestions())
                .unit(unit)
                .user(user)
                .build();

        Chapter savedChapter = chapterRepo.save(chapter);

        return mapToResponse(savedChapter);
    }

    public List<ChapterResponse> getAllChapters() {
        return chapterRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ChapterResponse getChapterById(Long id) {
        Chapter chapter = chapterRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
        return mapToResponse(chapter);
    }

    public ChapterResponse updateChapter(Long id, ChapterRequest request) {
        Chapter chapter = chapterRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));

        Unit unit = unitRepo.findById(request.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));

        chapter.setChapterName(request.getChapterName());
        chapter.setDescription(request.getDescription());
        chapter.setDisplayOrder(request.getDisplayOrder());
        chapter.setWeightage(request.getWeightage());
        chapter.setEstimatedQuestions(request.getEstimatedQuestions());
        chapter.setUnit(unit);

        Chapter updatedChapter = chapterRepo.save(chapter);
        return mapToResponse(updatedChapter);
    }

    public void deleteChapter(Long id) {
        Chapter chapter = chapterRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter not found"));
        chapterRepo.delete(chapter);
    }

    private ChapterResponse mapToResponse(Chapter chapter) {
        return ChapterResponse.builder()
                .chapterId(chapter.getChapterId())
                .chapterName(chapter.getChapterName())
                .description(chapter.getDescription())
                .displayOrder(chapter.getDisplayOrder())
                .weightage(chapter.getWeightage())
                .estimatedQuestions(chapter.getEstimatedQuestions())
                .unitId(chapter.getUnit().getUnitId())
                .unitName(chapter.getUnit().getUnitName())
                .build();
    }
}
