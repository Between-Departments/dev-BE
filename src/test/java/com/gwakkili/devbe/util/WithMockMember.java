package com.gwakkili.devbe.util;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockMemberSecurityContextFactory.class)
public @interface WithMockMember {

    long memberId() default 1;

    String mail() default "test@test1.ac.kr";

    String[] roles() default {"ROLE_USER"};

    String password() default "a12341234!";
}
