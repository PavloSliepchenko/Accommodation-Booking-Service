package com.example.accommodationbookingservice.dto.accommodation;

import com.example.accommodationbookingservice.dto.address.CreateAddressRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateAccommodationRequestDto {
    @NotBlank(message = "The accommodation type may not be blank")
    private String type;
    @NotNull(message = "The accommodation address may not be blank")
    private CreateAddressRequestDto location;
    @NotBlank(message = "The accommodation size may not be blank")
    private String size;
    private List<String> amenities;
    @NotNull(message = "The accommodation daily rate may not be blank")
    private BigDecimal dailyRate;
    @NotNull(message = "The accommodation availability may not be blank")
    private Integer availability;
}
