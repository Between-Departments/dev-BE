package com.gwakkili.devbe.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MajorValidator.class)
public @interface NicknameDuplicate {

    String message() default "이미 가입된 이메일 입니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
