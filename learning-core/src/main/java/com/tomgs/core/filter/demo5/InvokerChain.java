package com.tomgs.core.filter.demo5;

public interface InvokerChain<T> {

    void invoker(Invoker invoker, T t) throws Exception;
}
