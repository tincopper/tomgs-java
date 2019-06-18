package com.tomgs.core.filter.demo5;

public abstract class AbstractFilter<T> implements InvokerChain<T>, Filter {

    public void invoker(Invoker invoker, T t) throws Exception {
        doFilter(t);
        if (invoker != null) {
            invoker.invoker(t);
        } else {
            System.out.println("=====");
        }
        //doAfterFilter(t);
    }

    //protected abstract void doAfterFilter(T t) throws Exception;

    protected abstract void doFilter(T t) throws Exception;
}
