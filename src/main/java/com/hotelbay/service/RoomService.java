package com.hotelbay.service;

import com.hotelbay.entity.Room;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RoomService {
    private final Map<Long, Room> rooms = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Room save(Room room) {
        if (room.getId() == null) {
            room.setId(idGenerator.getAndIncrement());
        }
        rooms.put(room.getId(), room);
        return room;
    }

    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public List<Room> findByHotelId(Long hotelId) {
        return rooms.values().stream()
                .filter(r -> r.getHotel() != null && r.getHotel().getId().equals(hotelId))
                .toList();
    }

    public boolean existsById(Long id) {
        return rooms.containsKey(id);
    }

    public void deleteById(Long id) {
        rooms.remove(id);
    }

    public void deleteAll() {
        rooms.clear();
    }
}
