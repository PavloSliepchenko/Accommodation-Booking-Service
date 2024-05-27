package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingservice.dto.accommodation.CreateAccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationResponseDto addAccommodation(CreateAccommodationRequestDto requestDto);

    List<AccommodationResponseDto> getAccommodations(Pageable pageable);

    AccommodationResponseDto getAccommodationById(Long accommodationId);

    AccommodationResponseDto update(Long accommodationId,
                                    CreateAccommodationRequestDto requestDto);

    void delete(Long accommodationId);
}
