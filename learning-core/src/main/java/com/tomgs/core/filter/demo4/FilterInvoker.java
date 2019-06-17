package com.tomgs.core.filter.demo4;

public interface FilterInvoker {

    /**
     * 执行过滤
     */
    <T> void invoker(T t) throws Exception;
}
