package com.tomgs.core.reflect.jdkproxy;

public interface IDemo {

  void test();

  void test(String args);

  default void test1() {
    System.out.println("test1");
  }

}
