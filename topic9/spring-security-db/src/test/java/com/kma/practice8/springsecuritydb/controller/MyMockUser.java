package com.kma.practice8.springsecuritydb.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.test.context.support.WithSecurityContext;

import com.kma.practice8.springsecuritydb.domain.type.Permission;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MyUserSecurityContextFactory.class)
public @interface MyMockUser {

    String username() default "username";

    Permission[] authorities() default {};

    int companyId() default -1;

}
