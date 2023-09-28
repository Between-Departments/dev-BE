package com.gwakkili.devbe.validation;

import com.gwakkili.devbe.shcool.repository.MajorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MajorValidator implements ConstraintValidator<Major, String> {

    private final MajorRepository majorRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return majorRepository.existsByName(value);
    }
}
