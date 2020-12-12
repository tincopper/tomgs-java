package com.tomgs.core.reflect.cglibproxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author tomgs
 * @since 2020/12/9
 */
public class MyMethodInterceptor implements MethodInterceptor{

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
    System.out.println("这里是对目标类进行增强！！！");
    //注意这里的方法调用，不是用反射哦！！！
    Object object = proxy.invokeSuper(obj, args);
    return object;
  }

}
