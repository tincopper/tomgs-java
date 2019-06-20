package com.tomgs.es.gateway.common.component.filter;

import com.tomgs.es.gateway.common.exception.FilterException;

public interface Filter<T> {

    void filter(Invoker invoker, T t) throws FilterException;
}
