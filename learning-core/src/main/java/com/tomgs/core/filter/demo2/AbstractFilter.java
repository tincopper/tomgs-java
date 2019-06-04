package com.tomgs.core.filter.demo2;

/**
 * <pre>
 *
 *  File: AbstractFilter.java
 *
 *  Copyright (c) 2017, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2017年9月14日				jinwei				Initial.
 *
 * </pre>
 */
public abstract class AbstractFilter<T> implements Filter<T> {

    private final FilterFacade filterFacade;

    public AbstractFilter(final FilterFacade filterFacade) {
        this.filterFacade = filterFacade;
        this.register();
    }

    private void register() {
        filterFacade.register(this);
    }

    public void filter(FilterInvoker invoker, T t) throws Exception {
        doFilter(t);
        if (invoker != null) {
            invoker.invoker(t);
        }
    }

    protected abstract void doFilter(T t);

}
