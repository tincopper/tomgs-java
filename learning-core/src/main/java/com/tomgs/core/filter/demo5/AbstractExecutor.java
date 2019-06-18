package com.tomgs.core.filter.demo5;

/**
 *  
 *
 * @author tomgs
 * @version 2019/6/19 1.0 
 */
public abstract class AbstractExecutor<T> implements InvokerChain<T>, Executor {

    @Override
    public void invoker(Invoker invoker, T t) throws Exception {
        execute(t);
        if (invoker != null) {
            invoker.invoker(t);
        }
    }

    protected abstract void execute(T t);
}
