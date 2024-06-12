package com.example.accommodationbookingservice.repository;

import com.example.accommodationbookingservice.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query(value = "SELECT p "
            + "FROM Payment p "
            + "JOIN p.booking b "
            + "JOIN b.user u "
            + "WHERE u.id = :userId")
    List<Payment> findPaymentByUserId(Long userId);

    Optional<Payment> findByBookingId(Long bookingId);

    Optional<Payment> findBySessionId(String sessionId);
}
