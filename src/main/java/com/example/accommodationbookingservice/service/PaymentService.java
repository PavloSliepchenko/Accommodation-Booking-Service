package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.payment.PaymentResponseDto;
import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> getPaymentByUserId(Long userId);

    PaymentResponseDto addPayment(Long bookingId);
}
