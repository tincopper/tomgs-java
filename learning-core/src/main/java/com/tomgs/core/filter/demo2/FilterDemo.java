package com.tomgs.core.filter.demo2;

/**
 * @author tangzhongyuan
 * @since 2019-06-04 20:02
 **/
public class FilterDemo extends AbstractFilter<String> {

    public FilterDemo(FilterFacade filterFacade) {
        super(filterFacade);
    }

    @Override
    protected void doFilter(String str) {
        System.out.println("do filter: " + str);
    }
}
