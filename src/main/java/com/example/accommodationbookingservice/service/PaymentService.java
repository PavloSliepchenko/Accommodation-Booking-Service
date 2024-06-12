package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingservice.dto.payment.PaymentResponseFullInfoDto;
import com.example.accommodationbookingservice.model.Payment;
import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> getPaymentByUserId(Long userId);

    PaymentResponseDto addPayment(Long bookingId, Long userId);

    PaymentResponseFullInfoDto updatePaymentStatus(String sessionId, Payment.PaymentStatus status);
}
