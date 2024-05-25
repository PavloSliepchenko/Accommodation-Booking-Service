package com.example.accommodationbookingservice.controller;

import com.example.accommodationbookingservice.dto.user.UserLoginRequestDto;
import com.example.accommodationbookingservice.dto.user.UserLoginResponseDto;
import com.example.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.security.AuthenticationService;
import com.example.accommodationbookingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Tag(name = "Authentication", description = "Endpoints for the user registration and login")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register", description = "Saves a new user to DB")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.save(request);
    }

    @PostMapping(value = "/login")
    @Operation(summary = "Login",
            description = "Checks if there is such user in DB and returns a token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
