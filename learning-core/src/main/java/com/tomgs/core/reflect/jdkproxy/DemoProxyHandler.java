package com.tomgs.core.reflect.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author tomgs
 * @since 2020/12/9
 */
public class DemoProxyHandler implements InvocationHandler {

  RealDemo realDemo;

  public DemoProxyHandler(RealDemo realDemo) {
    this.realDemo = realDemo;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    realDemo.realTest();

    return null;
  }

}
