package com.tomgs.es.gateway.common.component.filter;

import com.tomgs.es.gateway.common.exception.FilterException;

public interface Invoker<T> {

    /**
     * 执行过滤
     */
    void invoker(T t) throws FilterException;
}
