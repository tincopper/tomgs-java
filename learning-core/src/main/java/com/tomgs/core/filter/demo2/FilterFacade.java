package com.tomgs.core.filter.demo2;

public class FilterFacade {

    private FilterInvoker invoker;

    public void register(final Filter filter) {
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