package com.tomgs.spring;

import com.google.common.collect.Maps;
import com.tomgs.spring.config.AppConfig;
import com.tomgs.spring.service.TestService;
import java.util.Map;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/11 1.0
 */
public class SpringDemo1 {

    private static final Map<String, Object> cache = Maps.newConcurrentMap();

    private static final Map<Class<?>, Object> cache2 = Maps.newConcurrentMap();

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(AppConfig.class);
        applicationContext.start();

        //TestService bean2 = applicationContext.getBean(TestService.class);
        //System.out.println(bean2);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) { // 637
            applicationContext.getBean("testService");
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) { // 637
            applicationContext.getBean(TestService.class);
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) { // 365
            cache.computeIfAbsent("testService", k -> applicationContext.getBean("testService"));
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) { // 637
            cache2.computeIfAbsent(TestService.class, k -> applicationContext.getBean(TestService.class));
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
