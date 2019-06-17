package com.tomgs.core.filter.demo4;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 20:02
 **/
public class FilterDemo extends AbstractFilter<String> {

    @Override
    protected void doAfterFilter(String str) throws Exception {
        System.out.println("FilterDemo do after filter: " + str);
    }

    @Override
    protected void doFilter(String str) {
        System.out.println("FilterDemo do filter: " + str);
    }
}
