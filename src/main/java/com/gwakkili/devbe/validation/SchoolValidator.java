package com.gwakkili.devbe.validation;

import com.gwakkili.devbe.major.repository.SchoolRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchoolValidator implements ConstraintValidator<School, String> {

    private final SchoolRepository schoolRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return schoolRepository.existsByName(value);
    }
}
