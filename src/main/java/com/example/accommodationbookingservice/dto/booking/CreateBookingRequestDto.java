package com.example.accommodationbookingservice.dto.booking;

import com.example.accommodationbookingservice.validator.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookingRequestDto {
    @NotBlank(message = "The checkIn may not be blank.")
    @Date
    private String checkIn;
    @NotBlank(message = "The checkOut may not be blank.")
    @Date
    private String checkOut;
    @NotNull(message = "The accommodationId may not be null.")
    private Long accommodationId;
}
