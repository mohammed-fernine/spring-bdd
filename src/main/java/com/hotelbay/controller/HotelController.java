package com.hotelbay.controller;

import com.hotelbay.entity.Hotel;
import com.hotelbay.entity.Review;
import com.hotelbay.service.HotelService;
import com.hotelbay.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "*")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.findAll();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelService.findById(id);
        return hotel.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Hotel>> getActiveHotels() {
        List<Hotel> activeHotels = hotelService.findByActive(true);
        return ResponseEntity.ok(activeHotels);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Hotel>> getInactiveHotels() {
        List<Hotel> inactiveHotels = hotelService.findByActive(false);
        return ResponseEntity.ok(inactiveHotels);
    }

    @GetMapping("/search/location/{location}")
    public ResponseEntity<List<Hotel>> searchByLocation(@PathVariable String location) {
        List<Hotel> hotels = hotelService.findByLocationContaining(location);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/search/active-location/{location}")
    public ResponseEntity<List<Hotel>> searchActiveByLocation(@PathVariable String location) {
        List<Hotel> hotels = hotelService.findActiveByLocationContaining(location);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location) {
        if (name != null && location != null) {
            List<Hotel> hotels = hotelService.findByNameOrLocationContaining(name, location);
            return ResponseEntity.ok(hotels);
        } else if (name != null) {
            Optional<Hotel> hotel = hotelService.findByName(name);
            return hotel.map(h -> ResponseEntity.ok(List.of(h)))
                    .orElse(ResponseEntity.notFound().build());
        } else if (location != null) {
            List<Hotel> hotels = hotelService.findByLocationContaining(location);
            return ResponseEntity.ok(hotels);
        } else {
            return ResponseEntity.ok(hotelService.findAll());
        }
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
        Hotel savedHotel = hotelService.save(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @Valid @RequestBody Hotel hotelDetails) {
        Optional<Hotel> optionalHotel = hotelService.findById(id);
        if (!optionalHotel.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Hotel hotel = optionalHotel.get();
        hotel.setName(hotelDetails.getName());
        hotel.setDescription(hotelDetails.getDescription());
        hotel.setLocation(hotelDetails.getLocation());
        hotel.setContactInfo(hotelDetails.getContactInfo());
        hotel.setServices(hotelDetails.getServices());
        hotel.setAmenities(hotelDetails.getAmenities());
        hotel.setCheckInTime(hotelDetails.getCheckInTime());
        hotel.setCheckOutTime(hotelDetails.getCheckOutTime());
        hotel.setCancellationPolicy(hotelDetails.getCancellationPolicy());
        hotel.setReservationConstraints(hotelDetails.getReservationConstraints());
        hotel.setActive(hotelDetails.getActive());

        Hotel updatedHotel = hotelService.save(hotel);
        return ResponseEntity.ok(updatedHotel);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Hotel> activateHotel(@PathVariable Long id) {
        Optional<Hotel> optionalHotel = hotelService.findById(id);
        if (!optionalHotel.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Hotel hotel = optionalHotel.get();
        hotel.setActive(true);
        Hotel updatedHotel = hotelService.save(hotel);
        return ResponseEntity.ok(updatedHotel);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Hotel> deactivateHotel(@PathVariable Long id) {
        Optional<Hotel> optionalHotel = hotelService.findById(id);
        if (!optionalHotel.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Hotel hotel = optionalHotel.get();
        hotel.setActive(false);
        Hotel updatedHotel = hotelService.save(hotel);
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        if (!hotelService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getHotelReviews(@PathVariable Long id) {
        if (!hotelService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Review> reviews = reviewService.findByHotelId(id);
        return ResponseEntity.ok(reviews);
    }
}
