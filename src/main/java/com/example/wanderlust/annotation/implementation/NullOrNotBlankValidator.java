package com.example.wanderlust.annotation.implementation;

import com.example.wanderlust.annotation.NullOrNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        log.debug("Parameters:: value: {}, context: {}", value, context);

        boolean result = value == null || !value.trim().isBlank();
        log.debug("Result: {}", result);

        return result;
    }
}
