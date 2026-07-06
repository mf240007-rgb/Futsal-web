package com.tugas.web.service;

import com.tugas.web.model.Booking;
import com.tugas.web.model.Lapang;
import com.tugas.web.model.User;
import com.tugas.web.repository.BookingRepository;
import com.tugas.web.repository.LapangRepository;
import com.tugas.web.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppDataService {
    
    @Autowired
    private LapangRepository lapangRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    @PostConstruct
    public void init() {
        // Seed data hanya jika database masih kosong
        if (lapangRepository.count() == 0) {
            lapangRepository.save(new Lapang("Lapang 1"));
            lapangRepository.save(new Lapang("Lapang 2"));
            lapangRepository.save(new Lapang("Lapang 3"));
            lapangRepository.save(new Lapang("Lapang 4"));
        }
        
        if (userRepository.count() == 0) {
            // Seed user demo
            userRepository.save(new User("user1", "user123", "USER"));
        }
    }

    // ── Lapang ──────────────────────────────────────────────────────────────

    public List<Lapang> getLapangs() {
        return lapangRepository.findAll();
    }

    public Optional<Lapang> findLapangById(Long id) {
        return lapangRepository.findById(id);
    }

    @Transactional
    public void saveLapang(Lapang lapang) {
        lapangRepository.save(lapang);
    }

    @Transactional
    public void updateLapangPhoto(Long lapangId, String photoPath) {
        findLapangById(lapangId).ifPresent(l -> {
            l.setPhotoPath(photoPath);
            lapangRepository.save(l);
        });
    }

    @Transactional
    public void deleteLapang(Long id) {
        lapangRepository.deleteById(id);
        // Hapus juga booking terkait
        bookingRepository.findAll().stream()
            .filter(b -> b.getLapangId().equals(id))
            .forEach(b -> bookingRepository.delete(b));
    }

    // ── User ─────────────────────────────────────────────────────────────────

    /** Semua user dengan role USER saja, tidak termasuk admin hardcoded */
    public List<User> getUsers() {
        return userRepository.findByRole("USER");
    }

    public Optional<User> findUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsUser(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void addUser(User user) {
        if (user.getRegisteredAt() == null) {
            user.setRegisteredAt(java.time.LocalDateTime.now());
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.findByUsername(username).ifPresent(u -> userRepository.delete(u));
    }

    // ── Booking ──────────────────────────────────────────────────────────────

    /** Semua booking, terbaru di atas */
    public List<Booking> getBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    /** Booking milik user tertentu, terbaru di atas */
    public List<Booking> getBookingsByUser(String username) {
        return bookingRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    /** Booking hari ini, diurutkan by jam mulai */
    public List<Booking> getBookingsToday() {
        LocalDate today = LocalDate.now();
        return bookingRepository.findByDateOrderByStartTimeAsc(today);
    }

    @Transactional
    public boolean deleteBooking(Long bookingId, String username) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent() && booking.get().getUsername().equals(username)) {
            bookingRepository.deleteById(bookingId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteBookingAdmin(Long bookingId) {
        if (bookingRepository.existsById(bookingId)) {
            bookingRepository.deleteById(bookingId);
            return true;
        }
        return false;
    }

    public boolean isLapangAvailable(Long lapangId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return bookingRepository.isAvailable(lapangId, date, startTime, endTime);
    }

    @Transactional
    public void addBooking(Long lapangId, String username, LocalDate date, LocalTime startTime, LocalTime endTime) {
        String lapangName = findLapangById(lapangId).map(Lapang::getName).orElse("Lapang tidak ditemukan");
        Booking booking = new Booking(lapangId, lapangName, username, date, startTime, endTime);
        bookingRepository.save(booking);
    }
}
