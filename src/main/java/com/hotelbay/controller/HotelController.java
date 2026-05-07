package com.hotelbay.controller;

import com.hotelbay.entity.Hotel;
import com.hotelbay.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.findAll();
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.findById(id).orElse(null);
        if (hotel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Hotel>> getActiveHotels() {
        List<Hotel> activeHotels = hotelService.findByActive(true);
        return new ResponseEntity<>(activeHotels, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel savedHotel = hotelService.save(hotel);
        return new ResponseEntity<>(savedHotel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelDetails) {
        Hotel hotel = hotelService.findById(id).orElse(null);
        if (hotel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        hotel.setName(hotelDetails.getName());
        hotel.setDescription(hotelDetails.getDescription());
        hotel.setLocation(hotelDetails.getLocation());
        hotel.setContactInfo(hotelDetails.getContactInfo());
        hotel.setActive(hotelDetails.getActive());

        Hotel updatedHotel = hotelService.save(hotel);
        return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Hotel> activateHotel(@PathVariable Long id) {
        Hotel hotel = hotelService.findById(id).orElse(null);
        if (hotel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        hotel.setActive(true);
        Hotel updatedHotel = hotelService.save(hotel);
        return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Hotel> deactivateHotel(@PathVariable Long id) {
        Hotel hotel = hotelService.findById(id).orElse(null);
        if (hotel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        hotel.setActive(false);
        Hotel updatedHotel = hotelService.save(hotel);
        return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        if (!hotelService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        hotelService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
