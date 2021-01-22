package com.tomgs.core.concurrent;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/1/11
 */
public class CyclicBarrierTest {

  CyclicBarrier cyclicBarrier = new CyclicBarrier(1);

  @Test
  public void testCyclicBarrier() throws IOException {
    for (int i = 0; i < 5; i++) {
      int finalI = i;
      Thread t = new Thread(() -> {
        try {
          cyclicBarrier.await();
          Thread.sleep(3000);
        } catch (InterruptedException | BrokenBarrierException e) {
          e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": " + finalI);
      });
      t.start();
    }

    System.in.read();
  }


}
