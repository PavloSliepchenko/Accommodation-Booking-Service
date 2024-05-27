package com.example.accommodationbookingservice.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAddressRequestDto {
    @NotBlank(message = "The street may not be blank")
    private String street;
    @NotBlank(message = "The building may not be blank")
    private String building;
    private String apartment;
    @NotBlank(message = "The city may not be blank")
    private String city;
    @NotBlank(message = "The country may not be blank")
    private String country;
}
