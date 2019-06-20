package com.tomgs.es.gateway.common.component.filter;

import com.tomgs.es.gateway.common.exception.FilterException;

public abstract class AbstractFilter<T> implements Filter<T> {

    public void filter(Invoker invoker, T t) throws FilterException {
        doFilter(t);
        if (invoker != null) {
            invoker.invoker(t);
        }
    }

    protected abstract void doFilter(T t) throws FilterException;
}
