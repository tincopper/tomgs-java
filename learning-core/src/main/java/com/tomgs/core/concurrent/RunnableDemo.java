package com.tomgs.core.concurrent;

/**
 * @author tangzy
 * @since 1.0
 */
public class RunnableDemo {

  public static void main(String[] args) {
    Thread t = new Thread(() -> {
      System.out.println("-----------start----------");
      for (int i = 0; i < 10; i++) {
        System.out.println("-----------123123----------");
        /*try {
          Thread.sleep(3000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }*/
      }
    });
    t.start();
    System.out.println("--------------------");
  }


}
