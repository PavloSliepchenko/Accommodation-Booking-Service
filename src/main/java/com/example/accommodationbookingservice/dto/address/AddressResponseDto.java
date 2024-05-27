package com.example.accommodationbookingservice.dto.address;

import lombok.Data;

@Data
public class AddressResponseDto {
    private Long id;
    private String street;
    private String building;
    private String apartment;
    private String city;
    private String country;
}
