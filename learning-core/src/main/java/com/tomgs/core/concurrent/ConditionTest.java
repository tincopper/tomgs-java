package com.tomgs.core.concurrent;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;

/**
 * @author tomgs
 * @since 2021/1/11
 */
public class ConditionTest {

  public Lock lock = new ReentrantLock();

  public Condition condition = lock.newCondition();

  @Test
  public void testCondition() throws IOException {
    for (int i = 0; i < 5; i++) {
      int finalI = i;
      Thread t = new Thread(() -> {
        try {
          lock.lock();
          try {
            condition.await();
            Thread.sleep(3000);
          } finally {
            lock.unlock();
          }

          lock.lock();
          try {
            condition.signal();
            Thread.sleep(3000);
          } finally {
            lock.unlock();
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": " + finalI);
      });
      t.start();
    }

    System.in.read();
  }

}
