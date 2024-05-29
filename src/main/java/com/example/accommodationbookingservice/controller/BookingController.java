package com.example.accommodationbookingservice.controller;

import com.example.accommodationbookingservice.dto.booking.BookingResponseDto;
import com.example.accommodationbookingservice.dto.booking.CreateBookingRequestDto;
import com.example.accommodationbookingservice.model.User;
import com.example.accommodationbookingservice.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bookings")
@Tag(name = "Bookings management", description = "Endpoints for CRUD operations with bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Add booking", description = "Allows to book an accommodation")
    public BookingResponseDto addBooking(Authentication authentication,
                                         @RequestBody @Valid CreateBookingRequestDto requestDto) {
        User user = getUser(authentication);
        return bookingService.save(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(summary = "Get booking by user id and booking status",
            description = "Retrieves bookings based on user ID "
                    + "and their status. (Available for managers)")
    public List<BookingResponseDto> getByUserIdAndStatus(@RequestParam Long userId,
                                                         @RequestParam String status) {
        return bookingService.getBookingsByIdAndStatus(userId, status);
    }

    @GetMapping(value = "/my")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "My bookings", description = "Retrieves user bookings")
    public List<BookingResponseDto> getMyBookings(Authentication authentication) {
        User user = getUser(authentication);
        return bookingService.getBookingsByUserId(user.getId());
    }

    @GetMapping(value = "/{bookingId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Get booking by id",
            description = "Provides information about a specific booking")
    public BookingResponseDto getBookingById(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @PatchMapping(value = "/{bookingId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Update booking",
            description = "Allows users to update their booking details")
    public BookingResponseDto updateBooking(Authentication authentication,
                                     @PathVariable Long bookingId,
                                     @RequestBody CreateBookingRequestDto requestDto) {
        User user = getUser(authentication);
        return bookingService.update(user.getId(), bookingId, requestDto);
    }

    @DeleteMapping(value = "/{bookingId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Cancel booking", description = "Enables the cancellation of bookings")
    public BookingResponseDto cancelBooking(Authentication authentication,
                                            @PathVariable Long bookingId) {
        User user = getUser(authentication);
        return bookingService.cancel(user.getId(), bookingId);
    }

    private User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
