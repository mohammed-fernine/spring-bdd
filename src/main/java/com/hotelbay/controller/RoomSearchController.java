package com.hotelbay.controller;

import com.hotelbay.entity.Room;
import com.hotelbay.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms/search")
@CrossOrigin(origins = "*")
public class RoomSearchController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/available")
    public ResponseEntity<List<Room>> searchAvailableRooms(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer guests,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut) {

        if (checkIn == null || checkOut == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Room> allRooms = roomService.findAll();
        
        List<Room> rooms = allRooms.stream()
                .filter(room -> room.getAvailable())
                .filter(room -> hotelId == null || (room.getHotel() != null && room.getHotel().getId().equals(hotelId)))
                .filter(room -> location == null || (room.getHotel() != null && room.getHotel().getLocation() != null && room.getHotel().getLocation().toLowerCase().contains(location.toLowerCase())))
                .filter(room -> guests == null || (room.getCapacity() != null && room.getCapacity() >= guests))
                .filter(room -> minPrice == null || (room.getPricePerNight() != null && room.getPricePerNight().compareTo(minPrice) >= 0))
                .filter(room -> maxPrice == null || (room.getPricePerNight() != null && room.getPricePerNight().compareTo(maxPrice) <= 0))
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.findByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<Room>> getAvailableRoomsByHotel(@PathVariable Long hotelId) {
        List<Room> rooms = roomService.findByHotelId(hotelId).stream()
                .filter(room -> room.getAvailable())
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/capacity/{minCapacity}")
    public ResponseEntity<List<Room>> getRoomsByCapacity(
            @PathVariable Integer minCapacity,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<Room> rooms = roomService.findAll().stream()
                .filter(room -> room.getCapacity() != null && room.getCapacity() >= minCapacity)
                .filter(room -> minPrice == null || (room.getPricePerNight() != null && room.getPricePerNight().compareTo(minPrice) >= 0))
                .filter(room -> maxPrice == null || (room.getPricePerNight() != null && room.getPricePerNight().compareTo(maxPrice) <= 0))
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/hotel/{hotelId}/capacity/{minCapacity}")
    public ResponseEntity<List<Room>> getHotelRoomsByCapacity(
            @PathVariable Long hotelId,
            @PathVariable Integer minCapacity,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<Room> rooms = roomService.findByHotelId(hotelId).stream()
                .filter(room -> room.getCapacity() != null && room.getCapacity() >= minCapacity)
                .filter(room -> minPrice == null || (room.getPricePerNight() != null && room.getPricePerNight().compareTo(minPrice) >= 0))
                .filter(room -> maxPrice == null || (room.getPricePerNight() != null && room.getPricePerNight().compareTo(maxPrice) <= 0))
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/available/all")
    public ResponseEntity<List<Room>> getAllAvailableRooms() {
        List<Room> rooms = roomService.findAll().stream()
                .filter(room -> room.getAvailable())
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }
}
