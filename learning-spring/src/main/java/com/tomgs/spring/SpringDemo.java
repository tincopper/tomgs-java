package com.tomgs.spring;

import com.tomgs.spring.config.AppConfig;
import com.tomgs.spring.service.UserService;
import com.tomgs.spring.tx.TxDemoService;
import org.springframework.beans.BeansException;
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

        try {
            //查询数据
            applicationContext.getBean(UserService.class).selectUsers();

            // 编程式事务
            applicationContext.getBean(TxDemoService.class).testMethod();
            applicationContext.getBean(TxDemoService.class).testTxWithTxm();
            // 声明式事务
            applicationContext.getBean(TxDemoService.class).testTxWithAnnotation();
        } catch (BeansException e) {
            e.printStackTrace();
        } finally {
            applicationContext.getBean(UserService.class).selectUsers();
        }

    }
}
