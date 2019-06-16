package com.tomgs.core.filter.demo3;

public interface FilterInvoker<T> {

    /**
     * 执行过滤
     */
    void invoker(T t) throws Exception;
}
