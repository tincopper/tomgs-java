package com.tomgs.core.oom;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *  元空间内存溢出
 *  -XX:MetaspaceSize=8m -XX:MaxMetaspaceSize=8m
 *
 *  java.lang.OutOfMemoryError: Metaspace
 *
 * @author tomgs
 * @version 2019/11/10 1.0 
 */
public class MetaspaceOOMDemo {

    static class OOMTest {

    }

    public static void main(String[] args) {
        int i = 0;
        try {
            while (true) {
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOMTest.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o, objects);
                    }
                });
                enhancer.create();
            }
        } catch (Throwable throwable) {
            System.out.println("*********** 多少次后发生异常：" + i);
            throwable.printStackTrace();
        }
    }
}
