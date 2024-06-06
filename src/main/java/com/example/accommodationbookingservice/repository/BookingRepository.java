package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.model.Booking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdAndStatus(Long userId, Booking.BookingStatus status);

    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByUserIdAndId(Long userId, Long id);

    List<Booking> findByStatus(Booking.BookingStatus status);
}
