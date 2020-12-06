package com.tomgs.spring.core;

import java.lang.annotation.*;

/**
 * 请求mapping
 *
 * @author tomgs
 * @version 2020/12/5 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

}
