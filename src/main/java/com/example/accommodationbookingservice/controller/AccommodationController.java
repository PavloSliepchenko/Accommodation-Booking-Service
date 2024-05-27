package com.example.accommodationbookingservice.controller;

import com.example.accommodationbookingservice.dto.accommodation.AccommodationResponseDto;
import com.example.accommodationbookingservice.dto.accommodation.CreateAccommodationRequestDto;
import com.example.accommodationbookingservice.service.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/accommodations")
@Tag(name = "Accommodation management",
        description = "Provides endpoints for CRUD operations with accommodations")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(summary = "Add accommodation",
            description = "Allows to add new accommodations. Available to managers only")
    public AccommodationResponseDto addAccommodation(
            @RequestBody @Valid CreateAccommodationRequestDto requestDto) {
        return accommodationService.addAccommodation(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Get accommodations",
            description = "Provides a list of available accommodations")
    public List<AccommodationResponseDto> getAccommodations(Pageable pageable) {
        return accommodationService.getAccommodations(pageable);
    }

    @GetMapping(value = "/{accommodationId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Get accommodation by id",
            description = "Retrieves detailed information about a specific accommodation")
    public AccommodationResponseDto getAccommodationById(@PathVariable Long accommodationId) {
        return accommodationService.getAccommodationById(accommodationId);
    }

    @PatchMapping(value = "/{accommodationId}")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(summary = "Update accommodation",
            description = "Allows updates to accommodation details")
    public AccommodationResponseDto updateAccommodation(
            @PathVariable Long accommodationId,
            @RequestBody CreateAccommodationRequestDto requestDto
    ) {
        return accommodationService.update(accommodationId, requestDto);
    }

    @DeleteMapping(value = "/{accommodationId}")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(summary = "Delete accommodation",
            description = "Enables the removal of accommodations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccommodation(@PathVariable Long accommodationId) {
        accommodationService.delete(accommodationId);
    }
}
