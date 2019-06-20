package com.tomgs.es.gateway.common.component.filter;

import com.tomgs.es.gateway.common.exception.FilterException;

import java.util.List;

public class FilterFacade<T> {

    private Invoker<T> invoker;

    public FilterFacade(Invoker<T> invoker, List<Filter> filters) {
        this.invoker = invoker;
        if (!filters.isEmpty()) {
            for (int i = filters.size() - 1; i >= 0; i--) {
                register(filters.get(i));
            }
        }
    }

    public FilterFacade(List<Filter> filters) {
        if (!filters.isEmpty()) {
            for (int i = filters.size() - 1; i >= 0; i--) {
                register(filters.get(i));
            }
        }
    }

    private void register(final Filter filter) {
        final Invoker<T> next = invoker;
        invoker = t -> filter.filter(next, t);
    }

    public void filter(T t) throws FilterException {
        if (invoker == null) {
            return;
        }
        invoker.invoker(t);
    }

}