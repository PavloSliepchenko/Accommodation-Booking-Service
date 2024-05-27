package com.example.accommodationbookingservice.dto.accommodation;

import com.example.accommodationbookingservice.dto.address.AddressResponseDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AccommodationResponseDto {
    private Long id;
    private String type;
    private AddressResponseDto location;
    private String size;
    private List<String> amenities;
    private BigDecimal dailyRate;
    private Integer availability;
}
