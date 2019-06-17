package com.tomgs.learning.limiter.demo2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tangzhongyuan
 * @since 2019-06-14 17:17
 **/
public class TestDemo3 {

    private static volatile CountDownLatch countDown;

    private static final Integer requestQps = 100;
    private static final Integer count = 10;
    private static final AtomicInteger done = new AtomicInteger();
    private static final AtomicInteger passcount = new AtomicInteger();
    private static final AtomicInteger blockcount = new AtomicInteger();
    private static final RateLimiterController paceController = new RateLimiterController(20 * 1000, count);

    public static void main(String[] args) throws InterruptedException {
        countDown = new CountDownLatch(1);
        simulatePulseFlow();
        countDown.await();
        System.out.println("done");
        System.out.println("total pass:" + passcount.get() + ", total block:" + blockcount.get());
        System.exit(0);
    }

    private static void simulatePulseFlow() {
        for (int i = 0; i < requestQps; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long startTime = TimeUtil.currentTimeMillis();
                    final boolean pass = paceController.canPass(1);
                    if (pass == true) {
                        passcount.incrementAndGet();
                    } else {
                        blockcount.incrementAndGet();
                    }
                    long cost = TimeUtil.currentTimeMillis() - startTime;
                    System.out.println(
                            TimeUtil.currentTimeMillis() + " one request pass, cost " + cost + " ms");
                    /*Entry entry = null;
                    try {
                        entry = SphU.entry(KEY);
                    } catch (BlockException e1) {
                        block.incrementAndGet();
                    } catch (Exception e2) {
                        // biz exception
                    } finally {
                        if (entry != null) {
                            entry.exit();
                            pass.incrementAndGet();
                            long cost = TimeUtil.currentTimeMillis() - startTime;
                            System.out.println(
                                    TimeUtil.currentTimeMillis() + " one request pass, cost " + cost + " ms");
                        }
                    }*/

                    try {
                        TimeUnit.MILLISECONDS.sleep(5);
                    } catch (InterruptedException e1) {
                        // ignore
                    }

                    if (done.incrementAndGet() >= requestQps) {
                        countDown.countDown();
                    }
                }
            }, "Thread " + i);
            thread.start();
        }
    }
}
