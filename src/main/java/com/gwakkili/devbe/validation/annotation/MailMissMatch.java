package com.gwakkili.devbe.validation.annotation;

import com.gwakkili.devbe.validation.validator.MailMissMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MailMissMatchValidator.class)
public @interface MailMissMatch {

    String message() default "해당 학교의 메일이 아닙니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fieldName1();

    String fieldName2();
}
