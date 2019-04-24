package com.tomgs.guice.test;

/**
 * @author tangzhongyuan
 * @create 2019-04-23 17:05
 **/
public class TestDemo {

    public static void main(String[] args) {
        String serviceName = "com.tomgs.test.Dtest";
        String simpleName = serviceName.substring(serviceName.lastIndexOf('.') + 1);
        System.out.println(simpleName);
    }
}
