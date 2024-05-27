package com.example.accommodationbookingservice.controller;

import com.example.accommodationbookingservice.dto.user.RoleUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserWithRoleResponseDto;
import com.example.accommodationbookingservice.model.User;
import com.example.accommodationbookingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
@Tag(name = "User Controller", description = "Provides endpoints for CRUD operations with users")
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/me")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Get user's info",
            description = "Retrieves the profile information for the currently logged-in user")
    public UserResponseDto getUsersInfo(Authentication authentication) {
        User user = getUser(authentication);
        return userService.findById(user.getId());
    }

    @PutMapping(value = "/{userId}/role")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(summary = "Update user's role",
            description = "Endpoint for the manager to update users' roles")
    public UserWithRoleResponseDto updateRole(@PathVariable Long userId,
                                              @RequestBody RoleUpdateRequestDto requestDto) {
        return userService.updateRole(userId, requestDto);
    }

    @PatchMapping(value = "/me")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Update profile info",
            description = "Allows users to update their profile information")
    public UserResponseDto updateProfileInfo(Authentication authentication,
                                             @RequestBody UserRegistrationRequestDto requestDto) {
        User user = getUser(authentication);
        return userService.updateUserInfo(user.getId(), requestDto);
    }

    @DeleteMapping(value = "/me")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    @Operation(summary = "Delete personal profile",
            description = "Allows users to remove their profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProfile(Authentication authentication) {
        User user = getUser(authentication);
        userService.delete(user.getId());
    }

    @DeleteMapping(value = "/delete/{userId}")
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    @Operation(summary = "Delete user by id",
            description = "Allows manager user to delete users by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
    }

    private User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
