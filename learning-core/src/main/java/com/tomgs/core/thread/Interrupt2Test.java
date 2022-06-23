package com.tomgs.core.thread;

/**
 * 演示线程不会被中断
 *
 * 如果线程不处于waiting或者timed_wating状态，是不会被中断的，
 * 即只有抛出InterruptedException的才会被中断如，sleep、wait、join等方法。
 *
 * @author tomgs
 * @since 2021/3/2
 */
public class Interrupt2Test extends Thread {

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread(() -> {
      Thread thread = Thread.currentThread();
      System.out.println("t1::: " + thread);
      int a = 1;
      while (true) {
        // no op
        a++;
        System.out.println("a: " + a);
      }
    }, "t1");

    t1.start();
    System.out.println("main::: " + t1);
    Thread.sleep(1000);
    t1.interrupt();
  }

  // 自定义中断异常处理
  @Override
  public void interrupt(){
    try{
      System.out.println("自定义interrupt");
      //socket.close();//关闭套接字
    } catch(Exception ignored){

    } finally {
      super.interrupt();//中断上层
    }

  }

}
