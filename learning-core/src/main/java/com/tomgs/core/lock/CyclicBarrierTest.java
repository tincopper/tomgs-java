package com.tomgs.core.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier的使用：
 *  1、基于ReentrantLock + Condition实现
 *  2、
 *
 * @author tomgs
 * @version 2019/11/5 1.0 
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("=====屏障之后执行...");
        });
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("---------" + finalI);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "i").start();
        }
    }
}
