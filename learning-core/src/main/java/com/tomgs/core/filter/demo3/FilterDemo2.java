package com.tomgs.core.filter.demo3;

/**
 * @author tomgs
 * @version 2019/6/4 1.0
 */
public class FilterDemo2 extends AbstractFilter<String> {

    @Override
    protected void doFilter(String str) {
        //throw new IllegalArgumentException("str:" + str);
        str += "123";
        System.out.println("do filter2: " + str);
    }
}
