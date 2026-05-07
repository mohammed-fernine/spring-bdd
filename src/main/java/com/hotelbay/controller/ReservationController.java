package com.hotelbay.controller;

import com.hotelbay.entity.Reservation;
import com.hotelbay.entity.Room;
import com.hotelbay.entity.User;
import com.hotelbay.entity.Hotel;
import com.hotelbay.service.ReservationService;
import com.hotelbay.service.RoomService;
import com.hotelbay.service.UserService;
import com.hotelbay.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Reservation>> getReservationsByGuest(@PathVariable Long guestId) {
        List<Reservation> reservations = reservationService.findByGuestId(guestId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Reservation>> getReservationsByHotel(@PathVariable Long hotelId) {
        List<Reservation> reservations = reservationService.findByHotelId(hotelId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Reservation>> getReservationsByRoom(@PathVariable Long roomId) {
        List<Reservation> reservations = reservationService.findByRoomId(roomId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable Reservation.ReservationStatus status) {
        List<Reservation> reservations = reservationService.findByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/guest/{guestId}/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByGuestAndStatus(
            @PathVariable Long guestId, @PathVariable Reservation.ReservationStatus status) {
        List<Reservation> reservations = reservationService.findByGuestAndStatus(guestId, status);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/hotel/{hotelId}/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByHotelAndStatus(
            @PathVariable Long hotelId, @PathVariable Reservation.ReservationStatus status) {
        List<Reservation> reservations = reservationService.findByHotelAndStatus(hotelId, status);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        if (reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (reservation.getGuest() == null || reservation.getGuest().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (reservation.getHotel() == null || reservation.getHotel().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (reservation.getCheckInDate() == null || reservation.getCheckOutDate() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (reservation.getCheckInDate().isAfter(reservation.getCheckOutDate()) || 
            reservation.getCheckInDate().isEqual(reservation.getCheckOutDate())) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Room> roomOpt = roomService.findById(reservation.getRoom().getId());
        Optional<User> guestOpt = userService.findById(reservation.getGuest().getId());
        Optional<Hotel> hotelOpt = hotelService.findById(reservation.getHotel().getId());

        if (!roomOpt.isPresent() || !guestOpt.isPresent() || !hotelOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Room room = roomOpt.get();
        User guest = guestOpt.get();
        Hotel hotel = hotelOpt.get();

        if (!room.getAvailable()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        List<Reservation> conflictingReservations = reservationService.findConflictingReservations(
                room.getId(), reservation.getCheckInDate(), reservation.getCheckOutDate());

        if (!conflictingReservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        reservation.setRoom(room);
        reservation.setGuest(guest);
        reservation.setHotel(hotel);

        Reservation savedReservation = reservationService.save(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @Valid @RequestBody Reservation reservationDetails) {
        Optional<Reservation> optionalReservation = reservationService.findById(id);
        if (!optionalReservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        
        if (reservation.getStatus() == Reservation.ReservationStatus.COMPLETED || 
            reservation.getStatus() == Reservation.ReservationStatus.CANCELED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (reservationDetails.getRoom() != null) {
            Optional<Room> roomOpt = roomService.findById(reservationDetails.getRoom().getId());
            if (!roomOpt.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            reservation.setRoom(roomOpt.get());
        }

        reservation.setCheckInDate(reservationDetails.getCheckInDate());
        reservation.setCheckOutDate(reservationDetails.getCheckOutDate());
        reservation.setNumberOfGuests(reservationDetails.getNumberOfGuests());
        reservation.setTotalAmount(reservationDetails.getTotalAmount());

        Reservation updatedReservation = reservationService.save(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Reservation> confirmReservation(@PathVariable Long id) {
        Optional<Reservation> optionalReservation = reservationService.findById(id);
        if (!optionalReservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        if (reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            return ResponseEntity.badRequest().build();
        }

        reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
        Reservation updatedReservation = reservationService.save(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        Optional<Reservation> optionalReservation = reservationService.findById(id);
        if (!optionalReservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        if (reservation.getStatus() == Reservation.ReservationStatus.COMPLETED || 
            reservation.getStatus() == Reservation.ReservationStatus.CANCELED) {
            return ResponseEntity.badRequest().build();
        }

        reservation.setStatus(Reservation.ReservationStatus.CANCELED);
        Reservation updatedReservation = reservationService.save(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Reservation> completeReservation(@PathVariable Long id) {
        Optional<Reservation> optionalReservation = reservationService.findById(id);
        if (!optionalReservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        if (reservation.getStatus() != Reservation.ReservationStatus.CONFIRMED) {
            return ResponseEntity.badRequest().build();
        }

        reservation.setStatus(Reservation.ReservationStatus.COMPLETED);
        Reservation updatedReservation = reservationService.save(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (!reservationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
