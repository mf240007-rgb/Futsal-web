package com.tugas.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lapang")
public class Lapang {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "photo_path")
    private String photoPath; // relative path, e.g. "lapang-1.jpg"

    public Lapang() {}

    public Lapang(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    /** URL foto untuk template — fallback ke placeholder jika belum ada */
    public String getPhotoUrl() {
        if (photoPath == null || photoPath.isBlank()) {
            return "https://placehold.co/400x220/1a1a2e/4fc3f7?text=" + name.replace(" ", "+");
        }
        return "/uploads/" + photoPath;
    }
}
