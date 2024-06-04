package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;

public interface NotificationService {
    void sendNotification(BookingResponseDto responseDto);
}
