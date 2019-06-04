package com.tomgs.core.filter.demo3;

public abstract class AbstractFilter<T> implements Filter<T> {

    public void filter(FilterInvoker invoker, T t) throws Exception {
        doFilter(t);
        if (invoker != null) {
            invoker.invoker(t);
        }
    }

    protected abstract void doFilter(T t) throws Exception;
}
