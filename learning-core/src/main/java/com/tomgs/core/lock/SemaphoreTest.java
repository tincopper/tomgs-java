package com.tomgs.core.lock;

import java.util.concurrent.Semaphore;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/5 1.0 
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("=======" + finalI);
                    Thread.sleep(1000);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
