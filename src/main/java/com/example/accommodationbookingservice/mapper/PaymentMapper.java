package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.payment.PaymentResponseDto;
import com.example.accommodationbookingservice.dto.payment.PaymentResponseFullInfoDto;
import com.example.accommodationbookingservice.model.Payment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookingMapper.class)
public interface PaymentMapper {
    @Mapping(target = "status", ignore = true)
    PaymentResponseDto toDto(Payment payment);

    @Mapping(target = "userId", source = "booking.user.id")
    @Mapping(target = "amount", source = "amountToPay")
    PaymentResponseFullInfoDto toFullInfoDto(Payment payment);

    @AfterMapping
    default void setStatus(@MappingTarget PaymentResponseDto responseDto, Payment payment) {
        responseDto.setStatus(payment.getStatus().name());
    }
}
