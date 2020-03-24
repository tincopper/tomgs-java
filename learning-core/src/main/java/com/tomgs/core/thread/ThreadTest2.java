package com.tomgs.core.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟线程一秒钟执行的次数
 *
 * @author tangzy
 * @since 1.0
 */
public class ThreadTest2 {

  public static void main(String[] args) {
    AtomicInteger i = new AtomicInteger(0);
    AtomicBoolean cutOff = new AtomicBoolean(false);
    CountDownLatch latch = new CountDownLatch(1);
    long start = System.currentTimeMillis();
    System.out.println("start: " + start);
    Thread t = new Thread(() -> {
      for (;;) {
        try {
          latch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        i.incrementAndGet();
        if (cutOff.get()) {
          System.out.println("次数：" + i.incrementAndGet());
          long end = System.currentTimeMillis();
          System.out.println("end: " + end + ", cost: " + (end - start));
          Thread.currentThread().interrupt();
          break;
        }
      }
    });
    Thread timer = new Thread(() -> {
      latch.countDown();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      cutOff.set(true);
    });
    t.start();
    timer.start();
    System.out.println("-------------------");
  }

}
