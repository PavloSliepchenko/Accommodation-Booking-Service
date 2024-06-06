package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import java.util.List;

public interface NotificationService {
    void sendNotification(BookingResponseDto responseDto);

    void sendNotification(List<BookingResponseDto> dtos);
}
