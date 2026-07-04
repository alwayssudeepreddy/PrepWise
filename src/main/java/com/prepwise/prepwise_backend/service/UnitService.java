package com.prepwise.prepwise_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.prepwise.prepwise_backend.exception.DuplicateResourceException;
import com.prepwise.prepwise_backend.exception.ResourceNotFoundException;

import com.prepwise.prepwise_backend.dto.unit.UnitRequest;
import com.prepwise.prepwise_backend.dto.unit.UnitResponse;
import com.prepwise.prepwise_backend.entity.Subject;
import com.prepwise.prepwise_backend.entity.Unit;
import com.prepwise.prepwise_backend.entity.User;
import com.prepwise.prepwise_backend.repository.SubjectRepository;
import com.prepwise.prepwise_backend.repository.UnitRepository;
import com.prepwise.prepwise_backend.repository.UserRepository;

@Service
@Transactional
public class UnitService {

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private UserRepository userRepo;

    // Add Unit
    public UnitResponse addUnit(UnitRequest request) {

        if (unitRepo.existsByUnitName(request.getUnitName())) {
            throw new DuplicateResourceException("Unit already exists");
        }

        Subject subject = subjectRepo.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Unit unit = Unit.builder()
                .unitName(request.getUnitName())
                .description(request.getDescription())
                .subject(subject)
                .user(user)
                .build();

        Unit savedUnit = unitRepo.save(unit);

        return mapToResponse(savedUnit);
    }

   
    public List<UnitResponse> getAllUnits() {

        return unitRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UnitResponse getUnitById(Long id) {

        Unit unit = unitRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));

        return mapToResponse(unit);
    }

    public UnitResponse updateUnit(Long id, UnitRequest request) {

        Unit unit = unitRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));

        Subject subject = subjectRepo.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        unit.setUnitName(request.getUnitName());
        unit.setDescription(request.getDescription());
        unit.setSubject(subject);

        Unit updatedUnit = unitRepo.save(unit);

        return mapToResponse(updatedUnit);
    }

    
    public void deleteUnit(Long id) {

        Unit unit = unitRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found"));

        unitRepo.delete(unit);
    }


    private UnitResponse mapToResponse(Unit unit) {

        return UnitResponse.builder()
                .unitId(unit.getUnitId())
                .unitName(unit.getUnitName())
                .description(unit.getDescription())
                .subjectId(unit.getSubject().getSubjectId())
                .subjectName(unit.getSubject().getSubjectName())
                .build();
    }
}