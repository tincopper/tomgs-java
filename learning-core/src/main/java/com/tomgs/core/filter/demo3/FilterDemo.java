package com.tomgs.core.filter.demo3;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 20:02
 **/
public class FilterDemo extends AbstractFilter<String> {

    @Override
    protected void doFilter(String str) {
        str += "234";
        System.out.println("do filter1: " + str);
    }
}
