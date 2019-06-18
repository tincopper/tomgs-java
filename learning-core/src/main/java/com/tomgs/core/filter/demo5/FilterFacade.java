package com.tomgs.core.filter.demo5;

import java.util.List;

public class FilterFacade {

    private Invoker invoker;

    public FilterFacade(List<Filter> filters, List<Executor> invokers) {
        for (int i = invokers.size() - 1; i >= 0; i--) {
            buildInvokerChain((InvokerChain)invokers.get(i));
        }
        for (int i = filters.size() - 1; i >= 0; i--) {
            buildInvokerChain((InvokerChain)filters.get(i));
        }

    }

    private void buildInvokerChain(final InvokerChain filter) {
        final Invoker next = invoker;
        invoker = new Invoker() {
            @Override
            public <T> void invoker(T t) throws Exception {
                filter.invoker(next, t);
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