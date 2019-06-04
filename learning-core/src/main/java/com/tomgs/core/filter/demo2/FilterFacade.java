package com.tomgs.core.filter.demo2;

public class FilterFacade {

    private FilterInvoker invoker;

    public void register(final Filter filter) {
        final FilterInvoker next = invoker;
        invoker = new FilterInvoker() {

            @Override
            public <T> void invoker(T t) {
                filter.filter(next, t);
            }
        };
    }

    public <T> void filter(T t) {
        if (invoker == null) {
            return;
        }
        try {
            invoker.invoker(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}