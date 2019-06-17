package com.tomgs.core.filter.demo4;

public interface Filter<T> {

    void filter(FilterInvoker invoker, T t) throws Exception;
}
