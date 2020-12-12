package com.tomgs.core.reflect.cglibproxy;

/**
 * @author tomgs
 * @since 2020/12/9
 */
public class Dog {

  final public void run(String name) {
    System.out.println("狗" + name + "----run");
  }

  public void eat() {
    System.out.println("狗----eat");
  }

}
