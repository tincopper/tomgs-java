package com.tomgs.springboot;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
