package com.hotelbay.service;

import com.hotelbay.entity.Hotel;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class HotelService {
    private final Map<Long, Hotel> hotels = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Hotel save(Hotel hotel) {
        if (hotel.getId() == null) {
            hotel.setId(idGenerator.getAndIncrement());
        }
        hotels.put(hotel.getId(), hotel);
        return hotel;
    }

    public Optional<Hotel> findById(Long id) {
        return Optional.ofNullable(hotels.get(id));
    }

    public List<Hotel> findAll() {
        return new ArrayList<>(hotels.values());
    }

    public List<Hotel> findByActive(boolean active) {
        List<Hotel> result = new ArrayList<>();
        for (Hotel hotel : hotels.values()) {
            if (hotel.getActive() == active) {
                result.add(hotel);
            }
        }
        return result;
    }

    public boolean existsById(Long id) {
        return hotels.containsKey(id);
    }

    public void deleteById(Long id) {
        hotels.remove(id);
    }

    public void deleteAll() {
        hotels.clear();
    }
}
