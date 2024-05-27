package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.address.AddressResponseDto;
import com.example.accommodationbookingservice.dto.address.CreateAddressRequestDto;
import com.example.accommodationbookingservice.model.Address;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    AddressResponseDto toDto(Address address);

    Address toModel(CreateAddressRequestDto requestDto);
}
