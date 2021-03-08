package com.tomgs.core.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author tomgs
 * @since 2021/3/2
 */
public class InterruptTest extends Thread {

  public static void main(String[] args) throws InterruptedException {

    CountDownLatch countDownLatch = new CountDownLatch(1);

    Thread t1 = new Thread(() -> {
      try {
        Thread thread = Thread.currentThread();
        System.out.println("t1::: " + thread);
        while (true) {
          // no op
          Thread.sleep(1000);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
        countDownLatch.countDown();
      }
    }, "t1");

    t1.start();

    System.out.println("main::: " + t1);

    Thread.sleep(3000);

    t1.interrupt();

    countDownLatch.await();

    t1.start();
  }

  // 自定义中断异常处理
  @Override
  public void interrupt(){
    try{
      //socket.close();//关闭套接字
    } catch(Exception ignored){

    } finally {
      super.interrupt();//中断上层
    }

  }

}
