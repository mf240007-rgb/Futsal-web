package com.tugas.web.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "lapang_id", nullable = false)
    private Long lapangId;
    
    @Column(name = "lapang_name", nullable = false)
    private String lapangName;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Booking() {}

    public Booking(Long lapangId, String lapangName, String username,
                   LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.lapangId = lapangId;
        this.lapangName = lapangName;
        this.username = username;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLapangId() { return lapangId; }
    public void setLapangId(Long lapangId) { this.lapangId = lapangId; }

    public String getLapangName() { return lapangName; }
    public void setLapangName(String lapangName) { this.lapangName = lapangName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /** Format timestamp untuk ditampilkan di template tanpa #temporals */
    public String getCreatedAtFormatted() {
        if (createdAt == null) return "-";
        return String.format("%02d/%02d %02d:%02d",
                createdAt.getDayOfMonth(), createdAt.getMonthValue(),
                createdAt.getHour(), createdAt.getMinute());
    }

    /** Inisial username untuk avatar — null-safe */
    public String getUsernameInitial() {
        if (username == null || username.isEmpty()) return "?";
        return username.substring(0, 1).toUpperCase();
    }
}
