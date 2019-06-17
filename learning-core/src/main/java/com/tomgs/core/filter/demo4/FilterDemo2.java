package com.tomgs.core.filter.demo4;

/**
 * @author tomgs
 * @version 2019/6/4 1.0
 */
public class FilterDemo2 extends AbstractFilter<String> {

    @Override
    protected void doAfterFilter(String str) throws Exception {
        System.out.println("FilterDemo2 do after filter: " + str);
    }

    @Override
    protected void doFilter(String str) {
        //throw new IllegalArgumentException("str:" + str);
        System.out.println("FilterDemo2 do filter: " + str);
    }


}
