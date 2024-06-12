package com.example.accommodationbookingservice.dto.payment;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentResponseFullInfoDto {
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private BookingResponseDto booking;
}
