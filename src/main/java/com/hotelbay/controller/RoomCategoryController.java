package com.hotelbay.controller;

import com.hotelbay.entity.RoomCategory;
import com.hotelbay.service.RoomCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/room-categories")
@CrossOrigin(origins = "*")
public class RoomCategoryController {

    @Autowired
    private RoomCategoryService roomCategoryService;

    @GetMapping
    public ResponseEntity<List<RoomCategory>> getAllCategories() {
        List<RoomCategory> categories = roomCategoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomCategory> getCategoryById(@PathVariable Long id) {
        Optional<RoomCategory> category = roomCategoryService.findById(id);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/root")
    public ResponseEntity<List<RoomCategory>> getRootCategories() {
        List<RoomCategory> categories = roomCategoryService.findRootCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoomCategory> getCategoryByName(@PathVariable String name) {
        List<RoomCategory> categories = roomCategoryService.findByName(name);
        return categories.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(categories.get(0));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<RoomCategory>> getSubcategories(@PathVariable Long parentId) {
        List<RoomCategory> categories = roomCategoryService.findByParentCategory(parentId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<RoomCategory>> searchCategories(@PathVariable String name) {
        List<RoomCategory> categories = roomCategoryService.findByName(name);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<RoomCategory> createCategory(@Valid @RequestBody RoomCategory category) {
        if (category.getParentCategory() != null && 
            !roomCategoryService.existsById(category.getParentCategory().getId())) {
            return ResponseEntity.badRequest().build();
        }
        RoomCategory savedCategory = roomCategoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomCategory> updateCategory(@PathVariable Long id, @Valid @RequestBody RoomCategory categoryDetails) {
        Optional<RoomCategory> optionalCategory = roomCategoryService.findById(id);
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        RoomCategory category = optionalCategory.get();
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        if (categoryDetails.getParentCategory() != null) {
            Optional<RoomCategory> parentOpt = roomCategoryService.findById(categoryDetails.getParentCategory().getId());
            if (parentOpt.isPresent()) {
                category.setParentCategory(parentOpt.get());
            }
        } else {
            category.setParentCategory(null);
        }

        RoomCategory updatedCategory = roomCategoryService.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!roomCategoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        List<RoomCategory> subcategories = roomCategoryService.findByParentCategory(id);
        if (!subcategories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        roomCategoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
