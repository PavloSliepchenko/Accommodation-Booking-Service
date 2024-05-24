package com.example.accommodationbookingservice.service;

import com.example.accommodationbookingservice.dto.user.RoleUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UpdateUserInfoRequestDto;
import com.example.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserWithRoleResponseDto;

public interface UserService {
    UserResponseDto findById(Long userId);

    UserWithRoleResponseDto updateRole(Long userId, RoleUpdateRequestDto requestDto);

    UserResponseDto updateUserInfo(Long userId, UpdateUserInfoRequestDto updateRequestDto);

    UserResponseDto save(UserRegistrationRequestDto registrationRequestDto);
}
