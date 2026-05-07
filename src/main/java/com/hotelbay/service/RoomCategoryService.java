package com.hotelbay.service;

import com.hotelbay.entity.RoomCategory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class RoomCategoryService {

    private final List<RoomCategory> categories = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<RoomCategory> findAll() {
        return new ArrayList<>(categories);
    }

    public Optional<RoomCategory> findById(Long id) {
        return categories.stream()
                .filter(category -> category.getId().equals(id))
                .findFirst();
    }

    public List<RoomCategory> findByName(String name) {
        return categories.stream()
                .filter(category -> category.getName() != null && category.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<RoomCategory> findByParentCategory(Long parentCategoryId) {
        return categories.stream()
                .filter(category -> category.getParentCategory() != null && 
                        category.getParentCategory().getId().equals(parentCategoryId))
                .collect(Collectors.toList());
    }

    public List<RoomCategory> findRootCategories() {
        return categories.stream()
                .filter(category -> category.getParentCategory() == null)
                .collect(Collectors.toList());
    }

    public RoomCategory save(RoomCategory category) {
        if (category.getId() == null) {
            category.setId(idGenerator.getAndIncrement());
            if (category.getCreatedAt() == null) {
                category.setCreatedAt(LocalDateTime.now());
            }
            if (category.getUpdatedAt() == null) {
                category.setUpdatedAt(LocalDateTime.now());
            }
        } else {
            categories.removeIf(c -> c.getId().equals(category.getId()));
            category.setUpdatedAt(LocalDateTime.now());
        }
        categories.add(category);
        return category;
    }

    public void deleteById(Long id) {
        categories.removeIf(category -> category.getId().equals(id));
    }

    public boolean existsById(Long id) {
        return categories.stream().anyMatch(category -> category.getId().equals(id));
    }
}
