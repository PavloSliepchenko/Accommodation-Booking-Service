package com.example.accommodationbookingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.net.URL;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table(name = "payments")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE payments SET is_deleted = true WHERE id = ?")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private PaymentStatus status;
    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "booking_id")
    private Booking booking;
    @Column(name = "session_url", columnDefinition = "VARCHAR(255)")
    private URL sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;

    public enum PaymentStatus {
        PENDING,
        PAID
    }
}
