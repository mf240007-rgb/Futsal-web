package com.tugas.web.repository;

import com.tugas.web.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUsernameOrderByCreatedAtDesc(String username);
    List<Booking> findByDateOrderByStartTimeAsc(LocalDate date);
    List<Booking> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN false ELSE true END FROM Booking b " +
           "WHERE b.lapangId = :lapangId AND b.date = :date " +
           "AND b.startTime < :endTime AND :startTime < b.endTime")
    boolean isAvailable(@Param("lapangId") Long lapangId, 
                       @Param("date") LocalDate date,
                       @Param("startTime") LocalTime startTime, 
                       @Param("endTime") LocalTime endTime);
}
