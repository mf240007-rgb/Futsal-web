package com.tugas.web.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String role;
    
    private String email;
    private String phone;
    
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.registeredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(LocalDateTime registeredAt) { this.registeredAt = registeredAt; }

    public String getRegisteredAtFormatted() {
        if (registeredAt == null) return "-";
        return String.format("%02d/%02d/%04d %02d:%02d",
                registeredAt.getDayOfMonth(), registeredAt.getMonthValue(),
                registeredAt.getYear(), registeredAt.getHour(), registeredAt.getMinute());
    }

    public String getUsernameInitial() {
        if (username == null || username.isEmpty()) return "?";
        return username.substring(0, 1).toUpperCase();
    }
}
