package com.hotelbay.controller;

import com.hotelbay.entity.Room;
import com.hotelbay.entity.Hotel;
import com.hotelbay.service.RoomService;
import com.hotelbay.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.findById(id);
        return room.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.findByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> rooms = roomService.findAll().stream()
                .filter(Room::getAvailable)
                .toList();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<Room>> getAvailableRoomsByHotel(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.findByHotelId(hotelId).stream()
                .filter(Room::getAvailable)
                .toList();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
        if (room.getHotel() == null || !hotelService.existsById(room.getHotel().getId())) {
            return ResponseEntity.badRequest().build();
        }
        Room savedRoom = roomService.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @Valid @RequestBody Room roomDetails) {
        Optional<Room> optionalRoom = roomService.findById(id);
        if (!optionalRoom.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Room room = optionalRoom.get();
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setRoomType(roomDetails.getRoomType());
        room.setDescription(roomDetails.getDescription());
        room.setCapacity(roomDetails.getCapacity());
        room.setPricePerNight(roomDetails.getPricePerNight());
        room.setAvailable(roomDetails.getAvailable());

        if (roomDetails.getHotel() != null) {
            Optional<Hotel> hotelOpt = hotelService.findById(roomDetails.getHotel().getId());
            if (hotelOpt.isPresent()) {
                room.setHotel(hotelOpt.get());
            }
        }

        Room updatedRoom = roomService.save(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Room> activateRoom(@PathVariable Long id) {
        Optional<Room> optionalRoom = roomService.findById(id);
        if (!optionalRoom.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Room room = optionalRoom.get();
        room.setAvailable(true);
        Room updatedRoom = roomService.save(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Room> deactivateRoom(@PathVariable Long id) {
        Optional<Room> optionalRoom = roomService.findById(id);
        if (!optionalRoom.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Room room = optionalRoom.get();
        room.setAvailable(false);
        Room updatedRoom = roomService.save(room);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (!roomService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
