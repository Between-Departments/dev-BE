package com.gwakkili.devbe.validation;


import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MajorValidator.class)
public @interface Major {

    String message() default "전공을 찾을 수 없습니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
