package com.gwakkili.devbe.validation.validator;

import com.gwakkili.devbe.validation.annotation.Enum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<Enum, java.lang.Enum> {

    @Override
    public boolean isValid(java.lang.Enum value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        Class<?> reflectionEnumClass = value.getDeclaringClass();
        return Arrays.asList(reflectionEnumClass.getEnumConstants()).contains(value);
    }
}
