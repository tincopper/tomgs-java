package com.tomgs.core.filter.demo5;

/**
 *  
 *
 * @author tomgs
 * @version 2019/6/19 1.0 
 */
public class ExecutorDemo2 extends AbstractExecutor<String> {

    @Override
    protected void execute(String s) {
        System.out.println("ExecutorDemo2: " + s);
    }
}
