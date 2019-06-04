package com.tomgs.core.filter.demo3;

public interface Filter<T> {

    void filter(FilterInvoker invoker, T t) throws Exception;
}
