package com.prepwise.prepwise_backend.controller;

import jakarta.validation.Valid;

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

import com.prepwise.prepwise_backend.dto.unit.UnitRequest;
import com.prepwise.prepwise_backend.dto.unit.UnitResponse;
import com.prepwise.prepwise_backend.service.UnitService;



@RestController
@RequestMapping({"/api/units", "/api/units/"})
public class UnitController {
    @Autowired
    UnitService Service;
    @PostMapping
    public UnitResponse addUnit(@Valid @RequestBody UnitRequest request) {
        return Service.addUnit(request);
    }
     @GetMapping
     public  List<UnitResponse> getUnit() {
         return Service.getAllUnits();
     }
    @GetMapping("/{id}")
     public  UnitResponse getUnitById(@PathVariable Long id) {
         return Service.getUnitById(id);
     }

    @DeleteMapping("/{id}")
     public String delete(@PathVariable Long id) {
         Service.deleteUnit(id);
         return "Deleted Unit";
     }

     @PutMapping("/{id}")
     public UnitResponse updateUnit(@PathVariable Long id, @Valid @RequestBody UnitRequest request) {
         
         return Service.updateUnit(id, request);
     } 

     
 
    
}
