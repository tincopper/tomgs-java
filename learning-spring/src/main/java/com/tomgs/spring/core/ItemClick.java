package com.tomgs.spring.core;

import java.lang.annotation.*;

/**
 * 工具栏按钮点击事件
 *
 * @author tomgs
 * @version 2020/12/5 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ItemClick {

    /**
     * @return name of the button in tool bar identity
     */
    String value();
}
