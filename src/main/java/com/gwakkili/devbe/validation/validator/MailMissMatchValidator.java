package com.gwakkili.devbe.validation.validator;

import com.gwakkili.devbe.school.service.SchoolService;
import com.gwakkili.devbe.validation.annotation.MailMissMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailMissMatchValidator implements ConstraintValidator<MailMissMatch, Object> {

    private final SchoolService schoolService;

    private String fieldName1;
    private String fieldName2;
    private String message;

    @Override
    public void initialize(MailMissMatch constraintAnnotation) {
        message = constraintAnnotation.message();
        fieldName1 = constraintAnnotation.fieldName1();
        fieldName2 = constraintAnnotation.fieldName2();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String school = getFieldValue(value, fieldName1);
        String mail = getFieldValue(value, fieldName2);
        String schoolMail = schoolService.getSchoolMail(school);
        if (schoolMail != null && !schoolMail.equals(mail.split("@")[1])) {
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
