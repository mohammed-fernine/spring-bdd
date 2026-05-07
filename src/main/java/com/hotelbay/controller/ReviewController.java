package com.hotelbay.controller;

import com.hotelbay.entity.Review;
import com.hotelbay.entity.Hotel;
import com.hotelbay.entity.User;
import com.hotelbay.entity.Reservation;
import com.hotelbay.service.ReviewService;
import com.hotelbay.service.HotelService;
import com.hotelbay.service.UserService;
import com.hotelbay.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.findById(id);
        return review.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Review>> getReviewsByHotel(@PathVariable Long hotelId) {
        List<Review> reviews = reviewService.findByHotelId(hotelId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Review>> getReviewsByGuest(@PathVariable Long guestId) {
        List<Review> reviews = reviewService.findByGuestId(guestId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<Review>> getReviewsByReservation(@PathVariable Long reservationId) {
        List<Review> reviews = reviewService.findByReservationId(reservationId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/hotel/{hotelId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByHotel(@PathVariable Long hotelId) {
        if (!hotelService.existsById(hotelId)) {
            return ResponseEntity.notFound().build();
        }
        Double averageRating = reviewService.findAverageRatingByHotel(hotelId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/hotel/{hotelId}/count")
    public ResponseEntity<Long> getReviewCountByHotel(@PathVariable Long hotelId) {
        if (!hotelService.existsById(hotelId)) {
            return ResponseEntity.notFound().build();
        }
        Long count = reviewService.countReviewsByHotel(hotelId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/hotel/{hotelId}/top-rated")
    public ResponseEntity<List<Review>> getTopRatedReviewsByHotel(@PathVariable Long hotelId) {
        if (!hotelService.existsById(hotelId)) {
            return ResponseEntity.notFound().build();
        }
        List<Review> reviews = reviewService.findByHotelOrderByRatingDesc(hotelId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/rating-range")
    public ResponseEntity<List<Review>> getReviewsByRatingRange(
            @RequestParam Integer minRating,
            @RequestParam Integer maxRating) {
        List<Review> reviews = reviewService.findByRatingRange(minRating, maxRating);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        if (review.getHotel() == null || !hotelService.existsById(review.getHotel().getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (review.getGuest() == null || !userService.existsById(review.getGuest().getId())) {
            return ResponseEntity.badRequest().build();
        }
        if (review.getReservation() == null || !reservationService.existsById(review.getReservation().getId())) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Reservation> reservationOpt = reservationService.findById(review.getReservation().getId());
        if (reservationOpt.isPresent() && reservationOpt.get().getStatus() != Reservation.ReservationStatus.COMPLETED) {
            return ResponseEntity.badRequest().build();
        }

        List<Review> existingReviews = reviewService.findByHotelAndGuest(
                review.getHotel().getId(), review.getGuest().getId());
        if (!existingReviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Review savedReview = reviewService.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody Review reviewDetails) {
        Optional<Review> optionalReview = reviewService.findById(id);
        if (!optionalReview.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Review review = optionalReview.get();
        
        if (reviewDetails.getHotel() != null) {
            if (!hotelService.existsById(reviewDetails.getHotel().getId())) {
                return ResponseEntity.badRequest().build();
            }
            review.setHotel(reviewDetails.getHotel());
        }

        if (reviewDetails.getGuest() != null) {
            if (!userService.existsById(reviewDetails.getGuest().getId())) {
                return ResponseEntity.badRequest().build();
            }
            review.setGuest(reviewDetails.getGuest());
        }

        review.setDescription(reviewDetails.getDescription());
        review.setRating(reviewDetails.getRating());

        Review updatedReview = reviewService.save(review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (!reviewService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
