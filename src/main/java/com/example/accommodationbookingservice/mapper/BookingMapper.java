package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingservice.model.Booking;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {AccommodationMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(target = "checkIn", source = "checkIn", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "checkOut", source = "checkOut", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "accommodation", ignore = true)
    Booking toModel(CreateBookingRequestDto requestDto);

    @Mapping(target = "checkIn", source = "checkIn", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "checkOut", source = "checkOut", dateFormat = "dd/MM/yyyy")
    BookingResponseDto toDto(Booking booking);

    @AfterMapping
    default void setStatus(@MappingTarget BookingResponseDto responseDto, Booking booking) {
        responseDto.setStatus(booking.getStatus().name());
    }
}
