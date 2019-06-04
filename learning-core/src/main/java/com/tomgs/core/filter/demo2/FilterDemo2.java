package com.tomgs.core.filter.demo2;

/**
 * @author tomgs
 * @version 2019/6/4 1.0
 */
public class FilterDemo2 extends AbstractFilter<String> {

    public FilterDemo2(FilterFacade filterFacade) {
        super(filterFacade);
    }

    @Override
    protected void doFilter(String str) {
        throw new IllegalArgumentException("str:" + str);
    }
}
