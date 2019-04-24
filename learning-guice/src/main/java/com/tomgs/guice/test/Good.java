package com.tomgs.guice.test;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @author tangzhongyuan
 * @create 2019-04-22 11:19
 **/

@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
@Target({ FIELD, PARAMETER, METHOD, LOCAL_VARIABLE })
public @interface Good {

}
