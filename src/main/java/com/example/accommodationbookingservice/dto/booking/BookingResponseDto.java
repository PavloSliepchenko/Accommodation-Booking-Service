package com.example.accommodationbookingservice.dto.booking;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import lombok.Data;

@Data
public class BookingResponseDto {
    private Long id;
    private String checkIn;
    private String checkOut;
    private AccommodationResponseDto accommodation;
    private UserResponseDto user;
    private String status;
}
