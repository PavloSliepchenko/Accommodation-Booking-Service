package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.dto.booking.CreateBookingRequestDto;
import java.util.List;

public interface BookingService {
    BookingResponseDto save(Long userId, CreateBookingRequestDto requestDto);

    List<BookingResponseDto> getBookingsByIdAndStatus(Long userId, String status);

    List<BookingResponseDto> getBookingsByUserId(Long userId);

    BookingResponseDto getBookingById(Long bookingId);

    BookingResponseDto update(Long userId, Long bookingId, CreateBookingRequestDto requestDto);

    BookingResponseDto cancel(Long userId, Long bookingId);
}
