package com.example.accommodationbookingservice.dto.payment;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private Long id;
    private String status;
    private BookingResponseDto booking;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;
    private String description;
}
