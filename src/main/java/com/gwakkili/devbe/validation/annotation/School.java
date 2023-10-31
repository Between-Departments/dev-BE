package com.gwakkili.devbe.validation.annotation;

import com.gwakkili.devbe.validation.validator.SchoolValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SchoolValidator.class)
public @interface School {

    String message() default "대학을 찾을 수 없습니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
