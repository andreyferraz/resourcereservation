package com.example.resourcereservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.resourcereservation.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByResourceIdAndStartTimeBetween(Long resourceId, LocalDateTime start, LocalDateTime end);
}
