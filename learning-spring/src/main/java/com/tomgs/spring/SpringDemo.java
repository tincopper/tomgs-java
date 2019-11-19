package com.tomgs.spring;

import com.tomgs.spring.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/11 1.0 
 */
public class SpringDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(AppConfig.class);
        applicationContext.start();

    }
}
