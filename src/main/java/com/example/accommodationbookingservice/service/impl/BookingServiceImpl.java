package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.mapper.BookingMapper;
import com.example.accommodationbookingservice.model.Booking;
import com.example.accommodationbookingservice.repository.AccommodationRepository;
import com.example.accommodationbookingservice.repository.BookingRepository;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.service.BookingService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final AccommodationRepository accommodationRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponseDto save(Long userId, CreateBookingRequestDto requestDto) {
        Booking booking = bookingMapper.toModel(requestDto);
        booking.setUser(userRepository.findById(userId).get());
        booking.setStatus(Booking.BookingStatus.PENDING);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponseDto> getBookingsByIdAndStatus(Long userId, String status) {
        Booking.BookingStatus bookingStatus = Arrays.stream(Booking.BookingStatus.values())
                .filter(e -> e.name().equals(status.toUpperCase()))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no booking status " + status));
        return bookingRepository
                .findByUserIdAndStatus(userId, bookingStatus).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public BookingResponseDto getBookingById(Long bookingId) {
        return bookingMapper.toDto(bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no booking with id " + bookingId)));
    }

    @Override
    public BookingResponseDto update(Long userId,
                                     Long bookingId,
                                     CreateBookingRequestDto requestDto) {
        Booking booking = getBookingByUserIdAndBookingId(userId, bookingId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (requestDto.getCheckIn() != null) {
            booking.setCheckIn(LocalDate.parse(requestDto.getCheckIn(), formatter));
        }
        if (requestDto.getCheckOut() != null) {
            booking.setCheckOut(LocalDate.parse(requestDto.getCheckOut(), formatter));
        }
        Long accommodationId = requestDto.getAccommodationId();
        if (accommodationId != null) {
            booking.setAccommodation(
                    accommodationRepository.findById(accommodationId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "There is no accommodation with id " + accommodationId
                            ))
            );
        }
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto cancel(Long userId, Long bookingId) {
        Booking booking = getBookingByUserIdAndBookingId(userId, bookingId);
        booking.setStatus(Booking.BookingStatus.CANCELED);
        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    private Booking getBookingByUserIdAndBookingId(Long userId, Long bookingId) {
        return bookingRepository.findByUserIdAndId(userId, bookingId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User with id "
                                + userId
                                + " doesn't have booking with id "
                                + bookingId));
    }
}