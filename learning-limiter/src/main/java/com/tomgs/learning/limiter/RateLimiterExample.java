package com.tomgs.learning.limiter;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 限流测试体验
 *
 * @author tangzhongyuan
 * @since 2019-06-03 11:21
 **/
public class RateLimiterExample {

    // Guava 从速度来限流，从每秒中能够执行的次数来
    private static final RateLimiter rateLimiter = RateLimiter.create(1d);

    // 同时只能有三个线程工作 Java1.5  从同时处理的线程个数来限流
    private static final Semaphore semaphore = new Semaphore(3);

    @Test
    public void testSemahore() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).forEach((i) -> service.submit(RateLimiterExample::semaphoreLimiter));
    }

    @Test
    public void testRateLimiter() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).forEach((i) -> {
            service.submit(RateLimiterExample::rateLimiter);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static void rateLimiter() {
        try {
            if (rateLimiter.tryAcquire()) {
                System.out.println(Thread.currentThread().getName() + " is doing work...");
                TimeUnit.MILLISECONDS.sleep(1000);
            } else {
                System.out.println(Thread.currentThread().getName() + " is discard ...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void semaphoreLimiter() {
        try {
            if (semaphore.tryAcquire()) {
                System.out.println(Thread.currentThread().getName() + " is doing work...");
                TimeUnit.MILLISECONDS.sleep(1000);
            } else {
                System.out.println(Thread.currentThread().getName() + " is discard ...");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
