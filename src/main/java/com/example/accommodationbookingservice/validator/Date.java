package com.example.accommodationbookingservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Date {
    String message() default ": date cannot be in the past and "
            + "must be in the following format: dd/MM/yyyy";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
