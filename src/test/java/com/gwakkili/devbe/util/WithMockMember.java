package com.gwakkili.devbe.util;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockMemberSecurityContextFactory.class)
public @interface WithMockMember {
    String mail() default "mockMember@test.ac.kr";

    String[] roles() default { "ROLE_USER" };

    String password() default "password";
}
