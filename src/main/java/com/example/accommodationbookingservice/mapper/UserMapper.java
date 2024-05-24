package com.example.accommodationbookingservice.mapper;

import com.example.accommodationbookingservice.config.MapperConfig;
import com.example.accommodationbookingservice.dto.user.UserRegistrationRequestDto;
import com.example.accommodationbookingservice.dto.user.UserResponseDto;
import com.example.accommodationbookingservice.dto.user.UserWithRoleResponseDto;
import com.example.accommodationbookingservice.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "role", ignore = true)
    UserWithRoleResponseDto toWithRoleDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    @AfterMapping
    default void setRole(@MappingTarget UserWithRoleResponseDto responseDtoWithRole, User user) {
        responseDtoWithRole.setRole(user.getRole().name());
    }
}
