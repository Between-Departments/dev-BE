package com.gwakkili.devbe.validation.validator;

import com.gwakkili.devbe.school.service.SchoolService;
import com.gwakkili.devbe.validation.annotation.School;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchoolValidator implements ConstraintValidator<School, String> {

    private final SchoolService schoolService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return schoolService.checkSupportedSchool(value);
    }
}
