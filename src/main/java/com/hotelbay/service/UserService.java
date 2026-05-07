package com.hotelbay.service;

import com.hotelbay.entity.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public List<User> findAllGuests() {
        return users.values().stream()
                .filter(u -> u.getRole() == User.UserRole.GUEST)
                .toList();
    }

    public List<User> findAllAdmins() {
        return users.values().stream()
                .filter(u -> u.getRole() == User.UserRole.ADMIN)
                .toList();
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    public boolean existsByUsername(String username) {
        return users.values().stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    public void deleteById(Long id) {
        users.remove(id);
    }

    public void deleteAll() {
        users.clear();
    }
}
