package com.gwakkili.devbe.validation.validator;

import com.gwakkili.devbe.validation.annotation.PasswordConfirm;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirm, Object> {

    private String message;
    private String fieldName1;
    private String fieldName2;

    @Override
    public void initialize(PasswordConfirm constraintAnnotation) {
        message = constraintAnnotation.message();
        fieldName1 = constraintAnnotation.fieldName1();
        fieldName2 = constraintAnnotation.fieldName2();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        String password = getFieldValue(value, fieldName1);
        String passwordConfirm = getFieldValue(value, fieldName2);
        if (!password.equals(passwordConfirm)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(fieldName2)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private String getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        Field dateField;
        try {
            dateField = clazz.getDeclaredField(fieldName);
            dateField.setAccessible(true);
            Object target = dateField.get(object);
            if (target != null && !(target instanceof String)) {
                throw new ClassCastException("casting exception");
            }
            return (String) target;
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
        }
        throw new RuntimeException("Not Found Field");
    }
}
