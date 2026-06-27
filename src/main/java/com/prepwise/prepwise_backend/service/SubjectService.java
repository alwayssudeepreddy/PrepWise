package com.prepwise.prepwise_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prepwise.prepwise_backend.dto.subject.SubjectRequest;
import com.prepwise.prepwise_backend.dto.subject.SubjectResponse;
import com.prepwise.prepwise_backend.entity.Subject;
import com.prepwise.prepwise_backend.repository.SubjectRepository;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepo;

    public SubjectResponse addSubject(SubjectRequest request)
    {
       if (subjectRepo.existsBySubjectName(request.getSubjectName())) {
            throw new RuntimeException("Subject already exists");
        }

        Subject subject = Subject.builder()
                .subjectName(request.getSubjectName())
                .description(request.getDescription())
                .build();

        Subject saved = subjectRepo.save(subject);

        return SubjectResponse.builder()
                .subjectId(saved.getSubjectId())
                .subjectName(saved.getSubjectName())
                .build();
    }

    public List<SubjectResponse> getAllSubjects()
    {
      return subjectRepo.findAll()
                .stream()
                .map(subject -> SubjectResponse.builder()
                        .subjectId(subject.getSubjectId())
                        .subjectName(subject.getSubjectName())
                        .description(subject.getDescription())
                        .build())
                .toList();
    }

    public SubjectResponse getSubjectById(Long id)

    {
        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return SubjectResponse.builder()
               .subjectId(subject.getSubjectId())
               .subjectName(subject.getSubjectName())
               .description(subject.getDescription())
               .build();

    }

    public SubjectResponse updateSubject(Long id, SubjectRequest request)
    {

        Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.setSubjectName(request.getSubjectName());
        subject.setDescription(request.getDescription());
        

        Subject updated = subjectRepo.save(subject);

        return SubjectResponse.builder()
                .subjectId(updated.getSubjectId())
                .subjectName(updated.getSubjectName())
                .description(updated.getDescription())
                .build();
    }

    public void deleteSubject(Long id)
    {
            Subject subject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subjectRepo.delete(subject);
    }


}
