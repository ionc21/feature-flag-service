package com.mettle.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockedUserSecurityContextFactory.class)
public @interface WithMockedUser {


    long id() default 1L;
    String userName() default "vasia";

    String[] role() default {"ADMIN"};

}
