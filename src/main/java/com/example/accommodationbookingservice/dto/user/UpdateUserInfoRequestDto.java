package com.example.accommodationbookingservice.dto.user;

import lombok.Data;

@Data
public class UpdateUserInfoRequestDto {
    private String email;
    private String password;
    private String repeatPassword;
    private String firstName;
    private String lastName;
}
