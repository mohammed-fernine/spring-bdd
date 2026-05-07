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

    public Optional<Hotel> findByName(String name) {
        return hotels.values().stream()
                .filter(h -> h.getName().equals(name))
                .findFirst();
    }

    public List<Hotel> findAll() {
        return new ArrayList<>(hotels.values());
    }

    public List<Hotel> findByActive(boolean active) {
        return hotels.values().stream()
                .filter(h -> h.getActive() == active)
                .toList();
    }

    public List<Hotel> findByLocationContaining(String location) {
        return hotels.values().stream()
                .filter(h -> h.getLocation() != null && h.getLocation().contains(location))
                .toList();
    }

    public List<Hotel> findActiveByLocationContaining(String location) {
        return hotels.values().stream()
                .filter(h -> h.getActive() && h.getLocation() != null && h.getLocation().contains(location))
                .toList();
    }

    public List<Hotel> findByNameOrLocationContaining(String name, String location) {
        return hotels.values().stream()
                .filter(h -> (h.getName() != null && h.getName().contains(name)) ||
                            (h.getLocation() != null && h.getLocation().contains(location)))
                .toList();
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
