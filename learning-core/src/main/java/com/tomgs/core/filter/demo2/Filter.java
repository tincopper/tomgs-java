package com.tomgs.core.filter.demo2;

public interface Filter<T> {

    void filter(FilterInvoker invoker, T t);
}
