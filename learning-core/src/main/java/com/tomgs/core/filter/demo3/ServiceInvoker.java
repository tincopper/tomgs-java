package com.tomgs.core.filter.demo3;

/**
 *  
 *
 * @author tomgs
 * @version 2019/6/9 1.0 
 */
public class ServiceInvoker implements FilterInvoker<String> {

    @Override
    public void invoker(String str) throws Exception {
        System.out.println("real invoker ...");
    }
}
