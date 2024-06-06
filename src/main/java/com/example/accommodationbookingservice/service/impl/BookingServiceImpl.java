package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingservice.exception.CancellationException;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.NoAccommodationException;
import com.example.accommodationbookingservice.mapper.BookingMapper;
import com.example.accommodationbookingservice.model.Accommodation;
import com.example.accommodationbookingservice.model.Booking;
import com.example.accommodationbookingservice.repository.AccommodationRepository;
import com.example.accommodationbookingservice.repository.BookingRepository;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.service.BookingService;
import com.example.accommodationbookingservice.service.NotificationService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final AccommodationRepository accommodationRepository;
    private final NotificationService notificationService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponseDto save(Long userId, CreateBookingRequestDto requestDto) {
        Accommodation accommodation =
                accommodationRepository.findById(requestDto.getAccommodationId())
                        .orElseThrow(() ->
                                new EntityNotFoundException("There is no accommodation with id "
                                        + requestDto.getAccommodationId()));
        if (accommodation.getAvailability() < 1) {
            throw new NoAccommodationException("No available accommodation with id "
                    + requestDto.getAccommodationId() + " left");
        }
        accommodation.setAvailability(accommodation.getAvailability() - 1);
        accommodation = accommodationRepository.save(accommodation);
        Booking booking = bookingMapper.toModel(requestDto);
        booking.setAccommodation(accommodation);
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
        if (!booking.getStatus().equals(Booking.BookingStatus.CANCELED)) {
            booking.setStatus(Booking.BookingStatus.CANCELED);
            Accommodation accommodation = booking.getAccommodation();
            accommodation.setAvailability(accommodation.getAvailability() + 1);
            accommodationRepository.save(accommodation);
            return bookingMapper.toDto(bookingRepository.save(booking));
        }
        throw new CancellationException("This booking was canceled before");
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void checkOverdueRentals() {
        LocalDate today = LocalDate.now();
        List<Booking> bookings =
                bookingRepository.findByStatus(Booking.BookingStatus.PENDING).stream()
                        .filter(e -> e.getCheckOut().getDayOfMonth() - today.getDayOfMonth() == 1)
                        .toList();
        if (!bookings.isEmpty()) {
            bookings.forEach(e -> e.setStatus(Booking.BookingStatus.EXPIRED));
            bookings = bookingRepository.saveAllAndFlush(bookings);
        }
        notificationService.sendNotification(bookings);
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
