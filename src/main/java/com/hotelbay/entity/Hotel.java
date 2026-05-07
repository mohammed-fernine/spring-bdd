package com.hotelbay.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class Hotel {
    
    private Long id;
    
    @NotBlank(message = "Hotel name is required")
    @Size(max = 200, message = "Hotel name must not exceed 200 characters")
    private String name;
    
    private String description;
    
    @NotBlank(message = "Location is required")
    @Size(max = 500, message = "Location must not exceed 500 characters")
    private String location;
    
    @Size(max = 100, message = "Contact information must not exceed 100 characters")
    private String contactInfo;
    
    private String services;
    
    private String amenities;
    
    private String checkInTime = "15:00";
    
    private String checkOutTime = "11:00";
    
    private String cancellationPolicy;
    
    private String reservationConstraints;
    
    private Boolean active = true;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<Room> rooms;
    
    private List<Reservation> reservations;
    
    private List<Review> reviews;
    
    public Hotel() {}
    
    public Hotel(String name, String location) {
        this.name = name;
        this.location = location;
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
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    
    public String getServices() { return services; }
    public void setServices(String services) { this.services = services; }
    
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
    
    public String getCheckInTime() { return checkInTime; }
    public void setCheckInTime(String checkInTime) { this.checkInTime = checkInTime; }
    
    public String getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(String checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public String getCancellationPolicy() { return cancellationPolicy; }
    public void setCancellationPolicy(String cancellationPolicy) { this.cancellationPolicy = cancellationPolicy; }
    
    public String getReservationConstraints() { return reservationConstraints; }
    public void setReservationConstraints(String reservationConstraints) { this.reservationConstraints = reservationConstraints; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
    
    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
    
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }
}
