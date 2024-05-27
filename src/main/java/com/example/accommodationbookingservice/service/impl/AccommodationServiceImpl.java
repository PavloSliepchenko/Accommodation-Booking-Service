package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingservice.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.mapper.AccommodationMapper;
import com.example.accommodationbookingservice.mapper.AddressMapper;
import com.example.accommodationbookingservice.model.Accommodation;
import com.example.accommodationbookingservice.model.Address;
import com.example.accommodationbookingservice.repository.AccommodationRepository;
import com.example.accommodationbookingservice.repository.AddressRepository;
import com.example.accommodationbookingservice.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AccommodationResponseDto addAccommodation(CreateAccommodationRequestDto requestDto) {
        Address address = addressRepository.save(addressMapper.toModel(requestDto.getLocation()));
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        accommodation.setLocation(address);
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public List<AccommodationResponseDto> getAccommodations(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Override
    public AccommodationResponseDto getAccommodationById(Long accommodationId) {
        return accommodationMapper.toDto(getById(accommodationId));
    }

    @Override
    public AccommodationResponseDto update(Long accommodationId,
                                           CreateAccommodationRequestDto requestDto) {
        Accommodation accommodation = getById(accommodationId);
        if (requestDto.getLocation() != null) {
            Address address = addressRepository.save(
                    addressMapper.toModel(requestDto.getLocation())
            );
            accommodation.setLocation(address);
        }
        if (requestDto.getType() != null) {
            accommodation.setType(Accommodation.Type.valueOf(requestDto.getType().toUpperCase()));
        }
        if (requestDto.getSize() != null) {
            accommodation.setSize(requestDto.getSize());
        }
        if (requestDto.getAmenities() != null) {
            accommodation.setAmenities(requestDto.getAmenities());
        }
        if (requestDto.getAvailability() != null) {
            accommodation.setAvailability(requestDto.getAvailability());
        }
        if (requestDto.getDailyRate() != null) {
            accommodation.setDailyRate(requestDto.getDailyRate());
        }
        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public void delete(Long accommodationId) {
        accommodationRepository.delete(getById(accommodationId));
    }

    private Accommodation getById(Long accommodationId) {
        return accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There is no accommodation with id " + accommodationId));
    }
}
