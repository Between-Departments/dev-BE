package com.gwakkili.devbe.validation.validator;

import com.gwakkili.devbe.major.service.MajorService;
import com.gwakkili.devbe.validation.annotation.Major;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MajorValidator implements ConstraintValidator<Major, String> {

    private final MajorService majorService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return majorService.checkSupportedMajor(value);
    }
}
