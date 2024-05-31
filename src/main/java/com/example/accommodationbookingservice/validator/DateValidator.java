package com.example.accommodationbookingservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<Date, String> {
    @Override
    public void initialize(Date constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate checkIn = null;
        try {
            checkIn = LocalDate.parse(value, formatter);
        } catch (Exception e) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return !checkIn.isBefore(now);
    }
}
