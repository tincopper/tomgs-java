package com.tomgs.spring.core;

import java.lang.annotation.*;

/**
 * @author tomgs
 * @version 2020/12/6 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Page {

    /**
     * @return name of the page identity
     */
    String value();

}
