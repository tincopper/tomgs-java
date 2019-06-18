package com.tomgs.core.filter.demo5;

public interface Invoker {

    /**
     * 执行过滤
     */
    <T> void invoker(T t) throws Exception;
}
