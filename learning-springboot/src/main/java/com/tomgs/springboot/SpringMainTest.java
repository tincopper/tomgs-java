package com.tomgs.springboot;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  
 *
 * @author tomgs
 * @version 2019/10/28 1.0 
 */
public class SpringMainTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.getBean("");

        List<String> list = new ArrayList<>();
        Object[] objects = list.toArray();
        String[] strings = list.toArray(new String[]{});
    }
}
