package com.hotelbay.entity;

public class User {

    private Long id;
    private String name;
    private String username;
    private String email;
    private UserRole role;

    public User() {
    }

    public User(String name, String username, String email, UserRole role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public enum UserRole {
        ADMIN, GUEST
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
