package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.dto.payment.PaymentResponseFullInfoDto;
import com.example.accommodationbookingservice.model.Booking;
import java.util.List;

public interface NotificationService {
    void sendNotification(BookingResponseDto responseDto);

    void sendNotification(List<Booking> bookings);

    void sendNotification(PaymentResponseFullInfoDto responseDto);
}
