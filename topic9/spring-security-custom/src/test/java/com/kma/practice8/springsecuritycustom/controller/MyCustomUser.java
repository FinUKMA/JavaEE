package com.kma.practice8.springsecuritycustom.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.test.context.support.WithSecurityContext;

import com.kma.practice8.springsecuritycustom.domain.type.Permission;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MyCustomUserFactory.class)
public @interface MyCustomUser {

    String companyAlias() default "";

    Permission[] permissions() default  { };

}
