package com.tomgs.core.filter.demo3;

import java.util.List;

public class FilterFacade<T> {

    private FilterInvoker<T> invoker;

    public FilterFacade(FilterInvoker<T> invoker, List<Filter> filters) {
        this.invoker = invoker;
        if (!filters.isEmpty()) {
            for (int i = filters.size() - 1; i >= 0; i--) {
                register(filters.get(i));
            }
        }
    }

    private void register(final Filter filter) {
        final FilterInvoker<T> next = invoker;
        /*invoker = new FilterInvoker<T>() {
            @Override
            public void invoker(T t) throws Exception {
                filter.filter(next, t);
            }
        };*/
        invoker = t -> filter.filter(next, t);
    }

    public void filter(T t) throws Exception {
        if (invoker == null) {
            return;
        }
        invoker.invoker(t);
    }

}