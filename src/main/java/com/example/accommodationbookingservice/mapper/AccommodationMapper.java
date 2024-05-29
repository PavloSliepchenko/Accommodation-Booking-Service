package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingservice.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingservice.model.Accommodation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = AddressMapper.class)
public interface AccommodationMapper {
    AccommodationResponseDto toDto(Accommodation accommodation);

    @Mapping(target = "type", ignore = true)
    Accommodation toModel(CreateAccommodationRequestDto requestDto);

    @AfterMapping
    default void setAccommodationType(@MappingTarget Accommodation accommodation,
                                      CreateAccommodationRequestDto requestDto) {
        accommodation.setType(Accommodation.Type.valueOf(requestDto.getType().toUpperCase()));
    }

    @Named("GetAccommodationById")
    default Accommodation getById(Long accommodationId) {
        Accommodation accommodation = new Accommodation();
        accommodation.setId(accommodationId);
        return accommodation;
    }
}
