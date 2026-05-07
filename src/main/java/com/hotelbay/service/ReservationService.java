package com.hotelbay.service;

import com.hotelbay.entity.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {
    private final Map<Long, Reservation> reservations = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(idGenerator.getAndIncrement());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> findByGuestId(Long guestId) {
        return reservations.values().stream()
                .filter(r -> r.getGuest() != null && r.getGuest().getId().equals(guestId))
                .toList();
    }

    public List<Reservation> findByHotelId(Long hotelId) {
        return reservations.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId))
                .toList();
    }

    public List<Reservation> findByRoomId(Long roomId) {
        return reservations.values().stream()
                .filter(r -> r.getRoom() != null && r.getRoom().getId().equals(roomId))
                .toList();
    }

    public List<Reservation> findByStatus(Reservation.ReservationStatus status) {
        return reservations.values().stream()
                .filter(r -> r.getStatus() == status)
                .toList();
    }

    public List<Reservation> findByGuestAndStatus(Long guestId, Reservation.ReservationStatus status) {
        return reservations.values().stream()
                .filter(r -> r.getGuest() != null && r.getGuest().getId().equals(guestId) && r.getStatus() == status)
                .toList();
    }

    public List<Reservation> findByHotelAndStatus(Long hotelId, Reservation.ReservationStatus status) {
        return reservations.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId) && r.getStatus() == status)
                .toList();
    }

    public List<Reservation> findConflictingReservations(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return reservations.values().stream()
                .filter(r -> r.getRoom() != null && r.getRoom().getId().equals(roomId) &&
                            r.getStatus() != Reservation.ReservationStatus.CANCELED &&
                            ((checkIn.isBefore(r.getCheckOutDate()) && checkOut.isAfter(r.getCheckInDate())) ||
                             (checkIn.isEqual(r.getCheckInDate()) || checkOut.isEqual(r.getCheckOutDate()))))
                .toList();
    }

    public boolean existsById(Long id) {
        return reservations.containsKey(id);
    }

    public void deleteById(Long id) {
        reservations.remove(id);
    }

    public void deleteAll() {
        reservations.clear();
    }
}
