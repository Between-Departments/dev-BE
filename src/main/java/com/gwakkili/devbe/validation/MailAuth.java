package com.gwakkili.devbe.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MailAuthValidator.class)
public @interface MailAuth {

    String message() default "인증되지 않은 메일입니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
