package com.hotelbay.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Review {
    
    private Long id;
    
    private Hotel hotel;
    
    private User guest;
    
    private Reservation reservation;
    
    @NotBlank(message = "Review description is required")
    private String description;
    
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 10, message = "Rating must not exceed 10")
    private Integer rating;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Review() {}
    
    public Review(Hotel hotel, User guest, Reservation reservation, String description, Integer rating) {
        this.hotel = hotel;
        this.guest = guest;
        this.reservation = reservation;
        this.description = description;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    
    public User getGuest() { return guest; }
    public void setGuest(User guest) { this.guest = guest; }
    
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
