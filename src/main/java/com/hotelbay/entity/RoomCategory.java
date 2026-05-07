package com.hotelbay.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class RoomCategory {
    
    private Long id;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String name;
    
    private String description;
    
    private RoomCategory parentCategory;
    
    private List<RoomCategory> subcategories;
    
    private List<Room> rooms;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public RoomCategory() {}
    
    public RoomCategory(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public RoomCategory getParentCategory() { return parentCategory; }
    public void setParentCategory(RoomCategory parentCategory) { this.parentCategory = parentCategory; }
    
    public List<RoomCategory> getSubcategories() { return subcategories; }
    public void setSubcategories(List<RoomCategory> subcategories) { this.subcategories = subcategories; }
    
    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
