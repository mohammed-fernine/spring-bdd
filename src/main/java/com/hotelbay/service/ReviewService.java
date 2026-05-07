package com.hotelbay.service;

import com.hotelbay.entity.Review;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReviewService {
    private final Map<Long, Review> reviews = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Review save(Review review) {
        if (review.getId() == null) {
            review.setId(idGenerator.getAndIncrement());
        }
        reviews.put(review.getId(), review);
        return review;
    }

    public Optional<Review> findById(Long id) {
        return Optional.ofNullable(reviews.get(id));
    }

    public List<Review> findAll() {
        return new ArrayList<>(reviews.values());
    }

    public List<Review> findByHotelId(Long hotelId) {
        return reviews.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId))
                .toList();
    }

    public List<Review> findByGuestId(Long guestId) {
        return reviews.values().stream()
                .filter(r -> r.getGuest() != null && r.getGuest().getId().equals(guestId))
                .toList();
    }

    public List<Review> findByReservationId(Long reservationId) {
        return reviews.values().stream()
                .filter(r -> r.getReservation() != null && r.getReservation().getId().equals(reservationId))
                .toList();
    }

    public List<Review> findByHotelAndGuest(Long hotelId, Long guestId) {
        return reviews.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId) &&
                            r.getGuest() != null && r.getGuest().getId().equals(guestId))
                .toList();
    }

    public Double findAverageRatingByHotel(Long hotelId) {
        return reviews.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId))
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public Long countReviewsByHotel(Long hotelId) {
        return reviews.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId))
                .count();
    }

    public List<Review> findByHotelOrderByRatingDesc(Long hotelId) {
        return reviews.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId))
                .sorted((r1, r2) -> Integer.compare(r2.getRating(), r1.getRating()))
                .toList();
    }

    public List<Review> findByRatingRange(Integer minRating, Integer maxRating) {
        return reviews.values().stream()
                .filter(r -> r.getRating() >= minRating && r.getRating() <= maxRating)
                .toList();
    }

    public boolean existsById(Long id) {
        return reviews.containsKey(id);
    }

    public void deleteById(Long id) {
        reviews.remove(id);
    }

    public void deleteAll() {
        reviews.clear();
    }
}
