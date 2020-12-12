package com.tomgs.core.reflect.cglibproxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

/**
 * cglib 可用实现继承一个类然后再实现接口！！！强
 *
 * @author tomgs
 * @since 2020/12/9
 */
public class CglibProxy2 {

  public static void main(String[] args) {
    //在指定目录下生成动态代理类，我们可以反编译看一下里面到底是一些什么东西
    System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\java\\java_workapace");

    //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
    Enhancer enhancer = new Enhancer();
    //设置目标类的字节码文件
    enhancer.setSuperclass(Dog.class);
    enhancer.setInterfaces(new Class[] {IAnimal.class});
    //设置回调函数
    enhancer.setCallback(new MyMethodInterceptor());

    IAnimal animal = (IAnimal) enhancer.create();
    animal.run();

    //这里的creat方法就是正式创建代理类
    Dog proxyDog = (Dog) enhancer.create();
    //调用代理类的eat方法
    proxyDog.eat();

    if (proxyDog instanceof IAnimal) {
      System.out.println("dog is animal");
      IAnimal animal1 = (IAnimal) proxyDog;
      animal1.run();
    }
  }

}
