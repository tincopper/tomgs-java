package com.tomgs.spring.core;

import java.lang.annotation.*;

/**
 * 按钮点击事件
 *
 * @author tomgs
 * @version 2020/12/5 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Click {

    /**
     * @return name of the button identity
     */
    String value();

}
