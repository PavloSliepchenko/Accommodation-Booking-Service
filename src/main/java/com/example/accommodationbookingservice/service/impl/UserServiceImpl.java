package com.example.accommodationbookingservice.service.impl;

import com.example.accommodationbookingservice.dto.user.RoleUpdateRequestDto;
import com.example.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserWithRoleResponseDto;
import com.example.accommodationbookingservice.exception.EntityNotFoundException;
import com.example.accommodationbookingservice.exception.RegistrationException;
import com.example.accommodationbookingservice.mapper.UserMapper;
import com.example.accommodationbookingservice.model.User;
import com.example.accommodationbookingservice.repository.UserRepository;
import com.example.accommodationbookingservice.service.UserService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final User.Role DEFAULT_ROLE = User.Role.CUSTOMER;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto findById(Long userId) {
        return userMapper.toDto(getUserById(userId));
    }

    @Override
    public UserWithRoleResponseDto updateRole(Long userId, RoleUpdateRequestDto requestDto) {
        User user = getUserById(userId);
        if (!Arrays.stream(User.Role.values())
                .anyMatch(e -> e.name().equals(requestDto.role().toUpperCase()))) {
            throw new EntityNotFoundException("There is no role " + requestDto.role());
        }
        user.setRole(User.Role.valueOf(requestDto.role().toUpperCase()));
        return userMapper.toWithRoleDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateUserInfo(Long userId,
                                          UserRegistrationRequestDto updateRequestDto) {
        User user = getUserById(userId);
        if (updateRequestDto.getFirstName() != null) {
            user.setFirstName(updateRequestDto.getFirstName());
        }
        if (updateRequestDto.getLastName() != null) {
            user.setLastName(updateRequestDto.getLastName());
        }
        if (updateRequestDto.getEmail() != null
                && checkEmailAvailability(updateRequestDto.getEmail())) {
            user.setEmail(updateRequestDto.getEmail());
        }
        if (updateRequestDto.getPassword() != null) {
            if (!updateRequestDto.getPassword().equals(updateRequestDto.getRepeatPassword())) {
                throw new RegistrationException("'Password' and 'repeat password' must match!");
            }
            user.setPassword(passwordEncoder.encode(updateRequestDto.getPassword()));
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto save(UserRegistrationRequestDto registrationRequestDto) {
        checkEmailAvailability(registrationRequestDto.getEmail());
        User user = userMapper.toModel(registrationRequestDto);
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        user.setRole(DEFAULT_ROLE);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(getUserById(userId));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("There is no user with id " + userId)
        );
    }

    private boolean checkEmailAvailability(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RegistrationException(
                    String.format("The user with email %s already exists", email));
        }
        return true;
    }
}
