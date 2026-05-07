package com.hotelbay.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Room {
    
    private Long id;
    
    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Room number must not exceed 20 characters")
    private String roomNumber;
    
    @NotBlank(message = "Room type is required")
    @Size(max = 100, message = "Room type must not exceed 100 characters")
    private String roomType;
    
    private String description;
    
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per night must be greater than 0")
    private BigDecimal pricePerNight;
    
    private Boolean available = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Hotel hotel;
    
    private List<RoomCategory> categories;
    
    private List<Reservation> reservations;
    
    public Room() {}
    
    public Room(String roomNumber, String roomType, Integer capacity, BigDecimal pricePerNight, Hotel hotel) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.hotel = hotel;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }
    
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    
    public List<RoomCategory> getCategories() { return categories; }
    public void setCategories(List<RoomCategory> categories) { this.categories = categories; }
    
    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}
