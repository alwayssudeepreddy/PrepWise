package com.prepwise.prepwise_backend.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prepwise.prepwise_backend.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{

    Optional<Unit> findByUnitName(String unitName);

boolean existsByUnitName(String unitName);

    
}
