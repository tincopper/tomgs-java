package com.tomgs.core.filter.demo3;

public abstract class AbstractFilter<T> implements Filter<T> {

    public void filter(FilterInvoker invoker, T t) throws Exception {
        doFilter(t);
        if (invoker != null) {
            invoker.invoker(t);
        }
        //因为invoker没有返回值，所以这里没有后置处理filter
        //doPostFilter(t);
    }

    protected abstract void doFilter(T t) throws Exception;
}
