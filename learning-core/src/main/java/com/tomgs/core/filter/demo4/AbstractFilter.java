package com.tomgs.core.filter.demo4;

public abstract class AbstractFilter<T> implements Filter<T> {

    public void filter(FilterInvoker invoker, T t) throws Exception {
        doFilter(t);
        if (invoker != null) {
            invoker.invoker(t);
        } else {
            System.out.println("=====");
        }
        doAfterFilter(t);
    }

    protected abstract void doAfterFilter(T t) throws Exception;

    protected abstract void doFilter(T t) throws Exception;
}
