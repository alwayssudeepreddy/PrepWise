package com.prepwise.prepwise_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prepwise.prepwise_backend.dto.chapter.ChapterRequest;
import com.prepwise.prepwise_backend.dto.chapter.ChapterResponse;
import com.prepwise.prepwise_backend.service.ChapterService;

@RestController
@RequestMapping({"/api/chapters", "/api/chapters/"})
public class ChapterController {
    
    @Autowired
    private ChapterService chapterService;

    @PostMapping
    public ChapterResponse addChapter(@RequestBody ChapterRequest request) {
        
        return chapterService.addChapter(request);
    }

    @GetMapping
    public List<ChapterResponse> getChapters() {
        return chapterService.getAllChapters();
    }

    @GetMapping("/{id}")
    public ChapterResponse getChapterById(@PathVariable Long id) {
        return chapterService.getChapterById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return "Deleted Chapter";
    }

    @PutMapping("/{id}")
    public ChapterResponse updateChapter(@PathVariable Long id, @RequestBody ChapterRequest request) {
        return chapterService.updateChapter(id, request);
    }
}
