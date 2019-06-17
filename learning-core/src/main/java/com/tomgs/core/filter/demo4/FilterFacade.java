package com.tomgs.core.filter.demo4;

import java.util.List;

public class FilterFacade {

    private FilterInvoker invoker;

    public FilterFacade(List<Filter> filters) {
        for (int i = filters.size() - 1; i >= 0; i--) {
            register(filters.get(i));
        }
    }

    private void register(final Filter filter) {
        final FilterInvoker next = invoker;
        invoker = new FilterInvoker() {

            @Override
            public <T> void invoker(T t) throws Exception {
                filter.filter(next, t);
            }
        };
    }

    public <T> void filter(T t) throws Exception {
        if (invoker == null) {
            return;
        }
        invoker.invoker(t);
    }

}