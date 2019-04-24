package com.tomgs.guice.test;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD, LOCAL_VARIABLE })
public @interface Bad {
}

