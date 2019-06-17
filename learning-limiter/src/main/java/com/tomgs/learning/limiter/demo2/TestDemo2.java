package com.tomgs.learning.limiter.demo2;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author tangzhongyuan
 * @since 2019-06-14 15:41
 **/
public class TestDemo2 {


    @Test
    public void testPaceController_normal() throws InterruptedException {
        RateLimiterController paceController = new RateLimiterController(500, 10d);

        long start = TimeUtil.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            assertTrue(paceController.canPass(1));
        }
        long end = TimeUtil.currentTimeMillis();
        final long result = end - start;
        System.out.println(result);
        assertTrue(result > 400);
    }

    @Test
    public void testPaceController_timeout() throws InterruptedException {
        final RateLimiterController paceController = new RateLimiterController(500, 10d);

        final AtomicInteger passcount = new AtomicInteger();
        final AtomicInteger blockcount = new AtomicInteger();
        final CountDownLatch countDown = new CountDownLatch(1);

        final AtomicInteger done = new AtomicInteger();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean pass = paceController.canPass(1);

                    if (pass == true) {
                        passcount.incrementAndGet();
                    } else {
                        blockcount.incrementAndGet();
                    }
                    done.incrementAndGet();

                    if (done.get() >= 10) {
                        countDown.countDown();
                    }
                }

            }, "Thread " + i);
            thread.start();
        }

        countDown.await();
        System.out.println("pass:" + passcount.get());
        System.out.println("block" + blockcount.get());
        System.out.println("done" + done.get());
        assertTrue(blockcount.get() > 0);

    }

    @Test
    public void testPaceController_zeroattack() throws InterruptedException {
        RateLimiterController paceController = new RateLimiterController(500, 0d);
        for (int i = 0; i < 2; i++) {
            assertFalse(paceController.canPass(1));
            assertTrue(paceController.canPass(0));
        }
    }
}
