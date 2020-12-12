package com.tomgs.core.reflect.jdkproxy;

import java.lang.reflect.Proxy;

/**
 * @author tomgs
 * @since 2020/12/9
 */
public class Main {

  public static void main(String[] args) {
    RealDemo realDemo = new RealDemo();
    DemoProxyHandler handler = new DemoProxyHandler(realDemo);
    IDemo proxyInstance = (IDemo)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IDemo.class}, handler);
    proxyInstance.test();
    //proxyInstance.test("123");
    //proxyInstance.test1();
  }

}
